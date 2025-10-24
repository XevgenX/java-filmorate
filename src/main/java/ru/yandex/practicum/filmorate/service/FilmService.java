package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class FilmService {
    private final FilmStorage storage;
    private final FilmValidator validator;

    public List<Film> list() {
        return storage.findAll();
    }

    public Optional<Film> getById(Long id) {
        return storage.findById(id);
    }

    public Film create(Film film) {
        validator.validate(film);
        film.setId(null);
        return storage.save(film);
    }

    public Film update(Film film) {
        validator.validate(film);
        if (film.getId() == null || storage.findById(film.getId()).isEmpty()) {
            throw new NotFoundException("Такого Id не существует");
        }
        return storage.save(film);
    }

    public void delete(Long id) {
        storage.delete(id);
    }

    public void addLike(Long filmId, User user) {
        Film film = storage.findById(filmId).orElseThrow(() -> new NotFoundException("Фильма с таким id не найдено"));
        film.addLike(user.getId());
    }

    public void removeLike(Long filmId, User user) {
        Film film = storage.findById(filmId).orElseThrow(() -> new NotFoundException("Фильма с таким id не найдено"));
        film.removeLike(user.getId());
    }

    public List<Film> getMostPopularFilms(Integer count) {
        return storage.findAll().stream()
                .sorted(Comparator.comparing((Film film) -> film.getLikes().size()).reversed())
                .limit(count).collect(Collectors.toList());
    }
}