package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component("userDbStorage")
@Slf4j
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) {
        String sqlQuery = "insert into \"user\"(\"email\", \"login\", \"name\", \"birthday\") " +
                "values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setTimestamp(4, Timestamp.valueOf(user.getBirthday().atStartOfDay()));
            return stmt;
        }, keyHolder);
        int id = keyHolder.getKey().intValue();
        user.setId(id);
        return user;
    }

    @Override
    public User update(User user) {
        String sqlQuery = "update \"user\" set " +
                "\"email\" = ?, \"login\" = ?, \"name\" = ?, \"birthday\" = ? " +
                "where \"id\" = ?";
        if (jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()) == 0) {
            log.info("Пользователь с идентификатором {} не найден", user.getId());
            throw new UserNotFoundException("Пользователь с идентификатором " + user.getId() + " не найден.");
        }
        return user;
    }

    @Override
    public Collection<User> findAll() {
        String           sqlQuery = "select * from \"user\"";
        Collection<User> result   = jdbcTemplate.query(sqlQuery, this::mapRowToModel);
        if (result.isEmpty()) {
            log.info("Пользователи не найдены.");
            throw new UserNotFoundException("Пользователи не найдены.");
        } else {
            return result;
        }
    }

    @Override
    public User findById(Integer id) {
        String     sqlQuery = "select * from \"user\" where \"id\" = ?";
        List<User> result   = jdbcTemplate.query(sqlQuery, this::mapRowToModel, id);
        if (result.isEmpty()) {
            log.info("Пользователь с идентификатором {} не найден.", id);
            throw new UserNotFoundException("Пользователь с id: " + id + " не найден.");
        } else {
            log.info("Найден пользователь: {} {}", id, result.get(0).getEmail());
            return result.get(0);
        }
    }

    @Override
    public Collection<User> findByIds(Collection<Integer> ids) {
        String           inSql    = String.join(",", Collections.nCopies(ids.size(), "?"));
        String           sqlQuery = String.format("select * from \"user\" where \"id\" in (%s)", inSql);
        Collection<User> result   = jdbcTemplate.query(sqlQuery, this::mapRowToModel, ids.toArray());
        if (result.isEmpty()) {
            log.info("Пользователи с идентификаторами {} не найдены.", ids.toArray());
            throw new UserNotFoundException("Пользователи с идентификаторами " + ids + " не найдены.");
        } else {
            return result;
        }
    }

    private User mapRowToModel(ResultSet resultSet, int rowNum) throws SQLException {
        User result = new User();
        result.setId(resultSet.getInt("id"));
        result.setEmail(resultSet.getString("email"));
        result.setLogin(resultSet.getString("login"));
        result.setName(resultSet.getString("name"));
        result.setBirthday(resultSet.getDate("birthday").toLocalDate());
        return result;
    }
}
