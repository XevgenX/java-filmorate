package ru.yandex.practicum.filmorate.manager.impl;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractInMemoryManager <M> {
    protected abstract Map<Long, M> getStorage();

    protected void validateIdForUpdate(Long id) {
        if (id == null || !getStorage().keySet().contains(id)) {
            throw new ValidationException("Такого Id не существует");
        }
    }

    protected Long generateNextId() {
        Optional<Long> maxId = getStorage().keySet().stream().max(Long::compareTo);
        return maxId.map(max -> max + 1).orElse(0L);
    }
}
