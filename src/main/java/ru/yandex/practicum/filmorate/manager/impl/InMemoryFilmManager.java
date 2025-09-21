package ru.yandex.practicum.filmorate.manager.impl;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.manager.FilmManager;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class InMemoryFilmManager extends AbstractInMemoryManager<Film> implements FilmManager {
    private final Map<Long, Film> storage;
    private final FilmValidator validator;

    public InMemoryFilmManager(FilmValidator validator) {
        storage = new HashMap<>();
        this.validator = validator;
    }

    @Override
    public List<Film> list() {
        return storage.values().stream().toList();
    }

    @Override
    public Film create(Film film) {
        validator.validate(film);
        film.setId(generateNextId());
        storage.put(film.getId(), film);
        if (log.isDebugEnabled()) {
            log.debug("Создан фильм с id {} и названием {}", film.getId(), film.getName());
        }
        return film;
    }

    @Override
    public Film update(Film film) {
        validator.validate(film);
        validateIdForUpdate(film.getId());
        storage.put(film.getId(), film);
        if (log.isDebugEnabled()) {
            log.debug("Обновлен фильм с id {} и названием {}", film.getId(), film.getName());
        }
        return film;
    }


    @Override
    protected Map<Long, Film> getStorage() {
        return storage;
    }
}
