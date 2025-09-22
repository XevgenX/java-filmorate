package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.manager.FilmManager;
import ru.yandex.practicum.filmorate.manager.impl.InMemoryFilmManager;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;
import java.util.List;

@RestController
@RequestMapping("/film")
public class FilmController {
    private final FilmManager manager;

    public FilmController() {
        this.manager = new InMemoryFilmManager(new FilmValidator());
    }

    @GetMapping
    public List<Film> list() {
        return manager.list();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        return manager.create(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        return manager.update(film);
    }
}