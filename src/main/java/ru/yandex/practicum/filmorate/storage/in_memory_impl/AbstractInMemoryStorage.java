package ru.yandex.practicum.filmorate.storage.in_memory_impl;

import ru.yandex.practicum.filmorate.model.AbstractModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractInMemoryStorage<M extends AbstractModel> {
    private final Map<Long, M> storage = new HashMap<>();

    public List<M> findAll() {
        return storage.values().stream().toList();
    }

    public Optional<M> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public M save(M film) {
        if (film.getId() == null) {
            film.setId(generateNextId());
        }
        storage.put(film.getId(), film);
        return storage.get(film.getId());
    }

    public void delete(Long id) {
        storage.remove(id);
    }

    private Long generateNextId() {
        Optional<Long> maxId = storage.keySet().stream().max(Long::compareTo);
        return maxId.map(max -> max + 1).orElse(1L);
    }
}