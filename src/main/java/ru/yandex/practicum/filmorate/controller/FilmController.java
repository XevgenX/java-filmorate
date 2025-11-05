package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validator.IdValidator;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private static final int TOP_LIKED_FILM_MAX_COUNT = 10;
    private final FilmService filmService;
    private final UserService userService;
    private final IdValidator idValidator;

    @GetMapping
    public List<Film> list() {
        return filmService.list();
    }

    @GetMapping("/{id}")
    public Film get(@PathVariable Long id) {
        idValidator.validate(id);
        return filmService.getById(id).orElseThrow(() -> new NotFoundException("Фильм с таким id не найден"));
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        return filmService.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void like(@PathVariable Long id, @PathVariable Long userId) {
        idValidator.validate(id);
        idValidator.validate(userId);
        User user = userService.getById(userId).orElseThrow(() -> new NotFoundException("Пользователя с таким id не найден"));
        filmService.addLike(id, user);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void dislike(@PathVariable Optional<Long> id, @PathVariable Optional<Long> userId) {
        idValidator.validate(id);
        idValidator.validate(userId);
        User user = userService.getById(userId.get()).orElseThrow(() -> new NotFoundException("Пользователя с таким id не найден"));
        filmService.removeLike(id.get(), user);
    }

    @GetMapping("/popular")
    public Collection<Film> popular(@RequestParam(required = false) Optional<Integer> count) {
        Collection<Film> mostPopularFilms = filmService.getMostPopularFilms(count.orElse(TOP_LIKED_FILM_MAX_COUNT));
        return mostPopularFilms;
    }
}