package ru.yandex.practicum.filmorate.storage.db_impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.db_impl.mapper.UserWithFriendsExtractor;

import java.sql.PreparedStatement;
import java.util.*;

@Qualifier("user_storage_db_impl")
@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private static final String FIND_ALL_QUERY = """
        SELECT u.id, u.name as name,  u.login as login, u.email as email,
            u.birthday as birthday,
            f.friend_id as friend_id, f.status as friendship_status
        FROM users u
        LEFT JOIN user_friends f ON u.id = f.user_id;""";
    private static final String FIND_BY_ID_QUERY = """
        SELECT u.id, u.name as name,  u.login as login, u.email as email,
            u.birthday as birthday,
            f.friend_id as friend_id, f.status as friendship_status
        FROM users u
        LEFT JOIN user_friends f ON u.id = f.user_id
        WHERE u.id = ?;""";
    private static final String CREATE_QUERY = """
        INSERT INTO users(name, login, email, birthday)
        VALUES (?, ?, ?, ?);""";
    private static final String UPDATE_QUERY = """
            UPDATE users
            SET name = ?, login = ?, email = ?, birthday = ?
            WHERE id = ?;""";
    private static final String DELETE_BY_ID_QUERY = """
            DELETE
            FROM users u
            WHERE u.id = ?""";
    private static final String MAKE_FRIENDSHIP_QUERY = """
            INSERT INTO user_friends(user_id, friend_id, status)
            VALUES (?, ?, ?);""";
    private static final String RUIN_FRIENDSHIP_QUERY = """
            DELETE
            FROM user_friends
            WHERE user_id = ? AND friend_id = ?;""";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(FIND_ALL_QUERY, new UserWithFriendsExtractor());
    }

    @Override
    public Optional<User> findById(Long id) {
        Object[] objects = new Object[]{id};
        List<User> users = jdbcTemplate.query(FIND_BY_ID_QUERY, objects, new UserWithFriendsExtractor());
        if (Objects.isNull(users) || users.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(users.get(0));
    }

    @Override
    public User save(User user) {
        if (Objects.isNull(user.getId())) {
            return create(user);
        } else {
            return update(user);
        }
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(DELETE_BY_ID_QUERY, id);
    }

    @Override
    public void makeFriendship(Long firstUserId, Long secondUserId, FriendshipStatus status) {
        Optional<User> user = findById(firstUserId);
        Optional<User> friend = findById(secondUserId);
        if (user.isEmpty() || friend.isEmpty()) {
            throw new NotFoundException("Пользователя с таким id не существует");
        }
        if (user.get().getFriends().keySet().contains(secondUserId)) {
            ruinFriendship(firstUserId, secondUserId);
        }
        jdbcTemplate.update(MAKE_FRIENDSHIP_QUERY, firstUserId, secondUserId, status.getName());
    }

    @Override
    public void ruinFriendship(Long firstUserId, Long secondUserId) {
        Optional<User> user = findById(firstUserId);
        Optional<User> friend = findById(secondUserId);
        if (user.isEmpty() || friend.isEmpty()) {
            throw new NotFoundException("Пользователя с таким id не существует");
        }
        jdbcTemplate.update(RUIN_FRIENDSHIP_QUERY, firstUserId, secondUserId);
    }

    private User create(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(CREATE_QUERY, new String[]{"id"});
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getEmail());
            stmt.setObject(4, user.getBirthday());
            return stmt;
        }, keyHolder);
        return findById(keyHolder.getKey().longValue()).orElseThrow();
    }

    private User update(User user) {
        jdbcTemplate.update(UPDATE_QUERY, user.getName(), user.getLogin(),
                user.getEmail(), user.getBirthday(), user.getId());
        return findById(user.getId()).orElseThrow();
    }


}
