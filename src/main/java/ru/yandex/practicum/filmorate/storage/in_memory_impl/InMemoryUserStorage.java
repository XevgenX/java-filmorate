package ru.yandex.practicum.filmorate.storage.in_memory_impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@Component
public class InMemoryUserStorage extends AbstractInMemoryStorage<User> implements UserStorage {
    @Override
    public void makeFriendship(Long firstUserId, Long secondUserId, FriendshipStatus status) {
        User firstUser = findById(firstUserId).orElseThrow(() -> new NotFoundException("Не найден пользователь с таким id"));
        User secondUser = findById(secondUserId).orElseThrow(() -> new NotFoundException("Не найден пользователь с таким id"));
        firstUser.addFriend(secondUserId, status);
    }

    @Override
    public void ruinFriendship(Long firstUserId, Long secondUserId) {
        User firstUser = findById(firstUserId).orElseThrow(() -> new NotFoundException("Не найден пользователь с таким id"));
        User secondUser = findById(secondUserId).orElseThrow(() -> new NotFoundException("Не найден пользователь с таким id"));
        firstUser.removeFriend(secondUserId);
    }
}
