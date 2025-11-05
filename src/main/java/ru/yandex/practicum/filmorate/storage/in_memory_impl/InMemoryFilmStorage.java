package ru.yandex.practicum.filmorate.storage.in_memory_impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

@Component
public class InMemoryFilmStorage extends AbstractInMemoryStorage<Film> implements FilmStorage {
    @Override
    public void addLike(Long filmId, User user) {
        Film film = findById(filmId).orElseThrow(() -> new NotFoundException("Фильма с таким id не найдено"));
        film.addLike(user.getId());
    }

    @Override
    public void removeLike(Long filmId, User user) {
        Film film = findById(filmId).orElseThrow(() -> new NotFoundException("Фильма с таким id не найдено"));
        film.removeLike(user.getId());
    }
}
