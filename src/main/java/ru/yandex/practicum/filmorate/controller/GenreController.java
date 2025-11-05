package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.validator.IdValidator;

import java.util.List;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    private final GenreService service;
    private final IdValidator idValidator;

    @GetMapping
    public List<Genre> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public Genre get(@PathVariable Long id) {
        idValidator.validate(id);
        return service.getById(id).orElseThrow(() -> new NotFoundException("Жанр с таким id не найден"));
    }
}
