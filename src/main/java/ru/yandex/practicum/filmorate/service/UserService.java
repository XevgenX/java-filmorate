package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.*;

@Slf4j
@Service
public class UserService {
    private final UserStorage storage;
    private final UserValidator validator;

    public UserService(@Qualifier("user_storage_db_impl") UserStorage storage, UserValidator validator) {
        this.storage = storage;
        this.validator = validator;
    }

    public List<User> list() {
        return storage.findAll();
    }

    public Optional<User> getById(Long id) {
        return storage.findById(id);
    }

    public User create(User user) {
        validator.validate(user);
        processName(user);
        user.setId(null);
        return storage.save(user);
    }

    public User update(User user) {
        validator.validate(user);
        if (user.getId() == null || storage.findById(user.getId()).isEmpty()) {
            throw new NotFoundException("Такого Id не существует");
        }
        processName(user);
        return storage.save(user);
    }

    public void makeFriendship(Long firstUserId, Long secondUserId, FriendshipStatus status) {
        storage.makeFriendship(firstUserId, secondUserId, status);
    }

    public void ruinFriendship(Long firstUserId, Long secondUserId) {
        storage.ruinFriendship(firstUserId, secondUserId);
    }

    public Collection<User> findCommonFriends(Long firstUserId, Long secondUserId) {
        User firstUser = storage.findById(firstUserId).orElseThrow(() -> new NotFoundException("Не найден пользователь с таким id"));
        User secondUser = storage.findById(secondUserId).orElseThrow(() -> new NotFoundException("Не найден пользователь с таким id"));
        Set<User> commonFriends = new HashSet<>();
        Set<Long> firstUserFriends = firstUser.getFriends().keySet();
        Set<Long> secondUserFriends = secondUser.getFriends().keySet();
        commonFriends.addAll(firstUserFriends.stream()
                .filter(friend -> secondUserFriends.contains(friend))
                .filter(id -> storage.findById(id).isPresent())
                .map(id -> storage.findById(id).get())
                .toList());
        commonFriends.addAll(secondUserFriends.stream()
                .filter(friend -> firstUserFriends.contains(friend))
                .filter(id -> storage.findById(id).isPresent())
                .map(id -> storage.findById(id).get())
                .toList());
        return commonFriends;
    }

    private void processName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
