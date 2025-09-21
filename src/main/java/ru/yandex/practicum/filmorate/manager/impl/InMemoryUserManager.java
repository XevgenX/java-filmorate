package ru.yandex.practicum.filmorate.manager.impl;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.manager.UserManager;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class InMemoryUserManager extends AbstractInMemoryManager<User> implements UserManager {
    private final Map<Long, User> storage;
    private final UserValidator validator;

    public InMemoryUserManager(UserValidator validator) {
        storage = new HashMap<>();
        this.validator = validator;
    }

    @Override
    public List<User> list() {
        return storage.values().stream().toList();
    }

    @Override
    public User create(User user) {
        validator.validate(user);
        processName(user);
        user.setId(generateNextId());
        storage.put(user.getId(), user);
        if (log.isDebugEnabled()) {
            log.debug("Создан пользователь с id {} и логином {}", user.getId(), user.getLogin());
        }
        return user;
    }

    @Override
    public User update(User user) {
        validator.validate(user);
        validateIdForUpdate(user.getId());
        processName(user);
        storage.put(user.getId(), user);
        if (log.isDebugEnabled()) {
            log.debug("Обновлен пользователь с id {} и логином {}", user.getId(), user.getLogin());
        }
        return user;
    }

    @Override
    protected Map<Long, User> getStorage() {
        return storage;
    }

    private void processName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
