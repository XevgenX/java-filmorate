package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.manager.UserManager;
import ru.yandex.practicum.filmorate.manager.impl.InMemoryUserManager;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserManager manager;

    public UserController() {
        manager = new InMemoryUserManager(new UserValidator());
    }

    @GetMapping
    public List<User> list() {
        return manager.list();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return manager.create(user);
    }

    @PutMapping
    public User update(@RequestBody User user) {
        return manager.update(user);
    }
}
