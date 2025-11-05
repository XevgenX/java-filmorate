package ru.yandex.practicum.filmorate.storage.db_impl.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserWithFriendsExtractor implements ResultSetExtractor<List<User>> {

    @Override
    public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, User> map = new HashMap<>();
        User user = null;
        while (rs.next()) {
            Long id = rs.getLong("id");
            user = map.get(id);
            if (user == null) {
                user = new User();
                user.setId(id);
                user.setName(rs.getString("name"));
                user.setLogin(rs.getString("login"));
                user.setEmail(rs.getString("email"));
                user.setBirthday(rs.getDate("birthday").toLocalDate());
                user.setFriends(new HashMap<>());
                map.put(id, user);
            }
            long friendId = rs.getLong("friend_id");
            String statusString = rs.getString("friendship_status");
            FriendshipStatus status = FriendshipStatus.NEW;
            if (Objects.nonNull(statusString)) {
                status = FriendshipStatus.findByName(statusString).orElse(FriendshipStatus.NEW);
            }
            if (friendId > 0) {
                user.getFriends().put(friendId, status);
            }
        }
        return new ArrayList<>(map.values());
    }
}
