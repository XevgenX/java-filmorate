package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final MpaService mpaService;
    private final FilmStorage storage;
    private final FilmValidator validator;

    public FilmService(@Qualifier("film_storage_db_impl") FilmStorage storage, MpaService mpaService, FilmValidator validator) {
        this.storage = storage;
        this.mpaService = mpaService;
        this.validator = validator;
    }

    public List<Film> list() {
        return storage.findAll().stream()
                .peek(this::fillWithMpa).toList();
    }

    public Optional<Film> getById(Long id) {
        Optional<Film> film = storage.findById(id);
        return film.map(this::fillWithMpa);
    }

    public Film create(Film film) {
        validator.validate(film);
        film.setId(null);
        Film saved = storage.save(film);
        fillWithMpa(film);
        return saved;
    }

    public Film update(Film film) {
        validator.validate(film);
        if (film.getId() == null || storage.findById(film.getId()).isEmpty()) {
            throw new NotFoundException("Такого Id не существует");
        }
        Film saved = storage.save(film);
        fillWithMpa(film);
        return saved;
    }

    public void delete(Long id) {
        storage.delete(id);
    }

    public void addLike(Long filmId, User user) {
        storage.addLike(filmId, user);
    }

    public void removeLike(Long filmId, User user) {
        storage.removeLike(filmId, user);
    }

    public List<Film> getMostPopularFilms(Integer count) {
        return storage.findAll().stream()
                .sorted(Comparator.comparing((Film film) -> film.getLikes().size()).reversed())
                .limit(count).collect(Collectors.toList());
    }

    private Film fillWithMpa(Film film) {
        if (Objects.nonNull(film.getMpa()) && Objects.nonNull(film.getMpa().getId())) {
            Optional<Mpa> mpa = mpaService.getById(film.getMpa().getId());
            film.setMpa(mpa.orElseThrow(() -> new NotFoundException("MPA с таком id не найден")));
            mpa.ifPresent(film::setMpa);
        }
        return film;
    }
}
