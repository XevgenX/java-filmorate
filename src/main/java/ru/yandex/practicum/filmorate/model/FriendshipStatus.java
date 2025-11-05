package ru.yandex.practicum.filmorate.model;

import java.util.Optional;

public enum FriendshipStatus {
    NEW("Запрос");
    private String name;

    FriendshipStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Optional<FriendshipStatus> findByName(String name) {
        for (FriendshipStatus status : FriendshipStatus.values()) {
            if (status.getName().equals(name)) {
                return Optional.of(status);
            }
        }
        return Optional.empty();
    }
}
