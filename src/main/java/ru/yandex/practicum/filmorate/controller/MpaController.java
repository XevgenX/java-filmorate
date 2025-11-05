package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.validator.IdValidator;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {
    private final MpaService service;
    private final IdValidator idValidator;

    @GetMapping
    public List<Mpa> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public Mpa get(@PathVariable Long id) {
        idValidator.validate(id);
        return service.getById(id).orElseThrow(() -> new NotFoundException("MPA с таким id не найден"));
    }
}
