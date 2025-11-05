package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorage storage;

    public List<Genre> list() {
        return storage.findAll();
    }

    public Optional<Genre> getById(Long id) {
        return storage.findById(id);
    }
}
