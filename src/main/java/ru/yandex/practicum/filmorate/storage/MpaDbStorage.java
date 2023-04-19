package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Component("mpaDbStorage")
@Slf4j
public class MpaDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void create(Mpa mpa) {
        String sqlQuery = "insert into \"mpa\"(\"name\") " +
                "values (?)";
        jdbcTemplate.update(sqlQuery,
                mpa.getName());
    }

    public Collection<Mpa> findAll() {
        String          sqlQuery = "select * from \"mpa\"";
        Collection<Mpa> result   = jdbcTemplate.query(sqlQuery, this::mapRowToModel);
        if (result.isEmpty()) {
            log.info("MPA не найдены.");
            throw new EntityNotFoundException(Mpa.class, "MPA не найдены.");
        } else {
            return result;
        }
    }

    public Mpa findById(Integer id) {
        String    sqlQuery = "select * from \"mpa\" where \"id\" = ?";
        List<Mpa> result   = jdbcTemplate.query(sqlQuery, this::mapRowToModel, id);
        if (result.isEmpty()) {
            log.info("MPA с идентификатором {} не найден.", id);
            throw new EntityNotFoundException(Mpa.class, "Mpa с id: " + id + " не найден.");
        } else {
            log.info("Найден mpa: {} {}", id, result.get(0).getName());
            return result.get(0);
        }
    }

    private Mpa mapRowToModel(ResultSet resultSet, int rowNum) throws SQLException {
        Mpa result = new Mpa();
        result.setId(resultSet.getInt("id"));
        result.setName(resultSet.getString("name"));
        return result;
    }
}
