package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.IdValidator;

import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final IdValidator idValidator;

    @GetMapping
    public List<User> list() {
        return userService.list();
    }

    @GetMapping("/{id}")
    public User get(@PathVariable Long id) {
        idValidator.validate(id);
        return userService.getById(id).orElseThrow(() -> new NotFoundException("Пользователь с таким id не найден"));
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable Long id) {
        idValidator.validate(id);
        return userService.getById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с таким id не найден"))
                .getFriends().stream()
                .filter(uid -> userService.getById(uid).isPresent())
                .map(uid -> userService.getById(uid).get())
                .toList();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    public User update(@RequestBody User user) {
        return userService.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void makeFriendship(@PathVariable Long id, @PathVariable Long friendId) {
        idValidator.validate(id);
        idValidator.validate(friendId);
        userService.makeFriendship(id, friendId);
        System.out.println("");
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void ruinFriendship(@PathVariable Long id, @PathVariable Long friendId) {
        idValidator.validate(id);
        idValidator.validate(friendId);
        userService.ruinFriendship(id, friendId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> commonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.findCommonFriends(id, otherId);
    }
}
