package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
public class User implements AbstractModel {
    private Long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private Map<Long, FriendshipStatus> friends = new HashMap<>();

    public void addFriend(Long id, FriendshipStatus status) {
        friends.put(id, status);
    }

    public void removeFriend(Long id) {
        friends.remove(id);
    }
}
