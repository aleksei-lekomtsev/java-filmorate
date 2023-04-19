package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Friend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component("friendDbStorage")
@Slf4j
public class FriendDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public FriendDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void create(Integer id, Integer friendId) {
        String sqlQuery = "insert into \"friend\"(\"user_id\", \"friend_id\") " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery,
                id,
                friendId);
    }

    public void delete(Integer id, Integer friendId) {
        String sqlQuery = "delete from \"friend\" where \"user_id\" = ? AND \"friend_id\" = ?";
        if (jdbcTemplate.update(sqlQuery, id, friendId) == 0) {
            log.info("Друзья для пользователя с идентификатором: {} не найдены", id);
            throw new EntityNotFoundException(Friend.class, "Друзья для пользователя с идентификатором " + id + " не найдены.");
        }
    }

    public Collection<Friend> findByUserId(Integer id) {
        String             sqlQuery = "select * from \"friend\" where \"user_id\" = ?";
        Collection<Friend> result   = jdbcTemplate.query(sqlQuery, this::mapRowToModel, id);
        return result;
    }

    private Friend mapRowToModel(ResultSet resultSet, int rowNum) throws SQLException {
        Friend result = new Friend();
        result.setId(resultSet.getInt("id"));
        result.setUserId(resultSet.getInt("user_id"));
        result.setFriendId(resultSet.getInt("friend_id"));
        return result;
    }
}
