package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.LikeNotFoundException;

import java.util.Collection;

@Component("likeDbStorage")
@Slf4j
public class LikeDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void create(Integer filmId, Integer userId) {
        String sqlQuery = "insert into \"like\"(\"film_id\", \"user_id\") " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery,
                filmId,
                userId);
    }

    public void delete(Integer filmId, Integer userId) {
        String sqlQuery = "delete from \"like\" where \"film_id\" = ? AND \"user_id\" = ?";
        if (jdbcTemplate.update(sqlQuery, filmId, userId) == 0) {
            log.info("Лайк для фильма с идентификатором {} и пользователя с идентификатором {} не найден", filmId, userId);
            throw new LikeNotFoundException(
                    "Лайк для фильма с идентификатором " + filmId + " и пользователем с идентификатором " +
                            userId + " не найден."
            );
        }
    }

    public Collection<Integer> findMostLikedFilms(Integer count) {
        String sqlQuery = "select \"film_id\" from \"like\" group by \"film_id\"" +
                " order by count(\"user_id\") desc limit ?";
        return jdbcTemplate.queryForList(sqlQuery, Integer.class, count);
    }
}
