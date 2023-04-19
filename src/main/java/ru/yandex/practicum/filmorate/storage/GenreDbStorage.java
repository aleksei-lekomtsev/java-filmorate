package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component("genreDbStorage")
@Slf4j
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void create(Genre genre) {
        String sqlQuery = "insert into \"genre\"(\"name\") " +
                "values (?)";
        jdbcTemplate.update(sqlQuery,
                genre.getName());
    }

    public Collection<Genre> findAll() {
        String            sqlQuery = "select * from \"genre\"";
        Collection<Genre> result   = jdbcTemplate.query(sqlQuery, this::mapRowToModel);
        if (result.isEmpty()) {
            log.info("Жанры не найдены.");
            throw new EntityNotFoundException(Genre.class, "Жанры не найдены.");
        } else {
            return result;
        }
    }

    public Genre findById(Integer id) {
        String      sqlQuery = "select * from \"genre\" where \"id\" = ?";
        List<Genre> result   = jdbcTemplate.query(sqlQuery, this::mapRowToModel, id);
        if (result.isEmpty()) {
            log.info("Жанр с идентификатором {} не найден.", id);
            throw new EntityNotFoundException(Genre.class, "Жанр с id: " + id + " не найден.");
        } else {
            log.info("Найден жанр: {} {}", id, result.get(0).getName());
            return result.get(0);
        }
    }

    public Collection<Genre> findByIds(Collection<Integer> ids) {
        String            inSql    = String.join(",", Collections.nCopies(ids.size(), "?"));
        String            sqlQuery = String.format("select * from \"genre\" where \"id\" in (%s)", inSql);
        Collection<Genre> result   = jdbcTemplate.query(sqlQuery, this::mapRowToModel, ids.toArray());
        return result;
    }

    private Genre mapRowToModel(ResultSet resultSet, int rowNum) throws SQLException {
        Genre result = new Genre();
        result.setId(resultSet.getInt("id"));
        result.setName(resultSet.getString("name"));
        return result;
    }
}
