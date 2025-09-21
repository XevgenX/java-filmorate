package ru.yandex.practicum.filmorate.manager;

import ru.yandex.practicum.filmorate.model.User;
import java.util.List;

public interface UserManager {
    List<User> list();
    User create(User user);
    User update(User user);
}
