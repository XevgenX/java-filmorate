package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    List<User> findAll();

    Optional<User> findById(Long id);

    User save(User user);

    void delete(Long id);

    void makeFriendship(Long firstUserId, Long secondUserId, FriendshipStatus status);

    void ruinFriendship(Long firstUserId, Long secondUserId);
}
