package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component("filmDbStorage")
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film create(Film film) {
        String sqlQuery = "insert into \"film\"(\"name\", \"description\", \"release_date\", \"duration\", \"mpa_id\") "
                + "values (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setTimestamp(3, Timestamp.valueOf(film.getReleaseDate().atStartOfDay()));
            stmt.setLong(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);

        int id = keyHolder.getKey().intValue();
        film.setId(id);
        return film;
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "update \"film\" set " +
                "\"name\" = ?, \"description\" = ?, \"release_date\" = ?, \"duration\" = ?, \"mpa_id\" = ? " +
                "where \"id\" = ?";
        if (jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()) == 0) {
            log.info("Фильм с идентификатором {} не найден", film.getId());
            throw new EntityNotFoundException(Film.class, "Фильм с идентификатором " + film.getId() + " не найден.");
        }
        return film;
    }

    @Override
    public Collection<Film> findAll() {
        String           sqlQuery = "select * from \"film\"";
        Collection<Film> result   = jdbcTemplate.query(sqlQuery, this::mapRowToModel);
        return result;
    }

    public Collection<Film> findByIds(Collection<Integer> ids) {
        String           inSql    = String.join(",", Collections.nCopies(ids.size(), "?"));
        String           sqlQuery = String.format("select * from \"film\" where \"id\" in (%s)", inSql);
        Collection<Film> result   = jdbcTemplate.query(sqlQuery, this::mapRowToModel, ids.toArray());
        return result;
    }

    @Override
    public Film findById(Integer id) {
        String     sqlQuery = "select * from \"film\" where \"id\" = ?";
        List<Film> result   = jdbcTemplate.query(sqlQuery, this::mapRowToModel, id);
        if (result.isEmpty()) {
            log.info("Фильм с идентификатором {} не найден.", id);
            throw new EntityNotFoundException(Film.class, "Фильм с id: " + id + " не найден.");
        } else {
            log.info("Найден фильм: {} {}", id, result.get(0).getName());
            return result.get(0);
        }
    }

    private Film mapRowToModel(ResultSet resultSet, int rowNum) throws SQLException {
        Film result = new Film();
        result.setId(resultSet.getInt("id"));
        result.setName(resultSet.getString("name"));
        result.setDescription(resultSet.getString("description"));
        result.setReleaseDate(resultSet.getDate("release_date").toLocalDate());
        result.setDuration(resultSet.getInt("duration"));
        result.getMpa().setId(resultSet.getInt("mpa_id"));
        return result;
    }
}
