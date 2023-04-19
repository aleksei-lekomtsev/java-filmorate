package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component("filmGenreDbStorage")
@Slf4j
public class FilmGenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmGenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public FilmGenre create(FilmGenre model) {
        String sqlQuery = "merge into \"film_genre\"(\"film_id\", \"genre_id\") " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery,
                model.getFilmId(),
                model.getGenreId());
        return model;
    }

    public Collection<FilmGenre> findByFilmId(Integer id) {
        String sqlQuery = "select * from \"film_genre\" where \"film_id\" = ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToModel, id);
    }

    public boolean delete(Integer filmId) {
        String sqlQuery = "delete from \"film_genre\" where \"film_id\" = ?";
        return jdbcTemplate.update(sqlQuery, filmId) > 0;
    }

    private FilmGenre mapRowToModel(ResultSet resultSet, int rowNum) throws SQLException {
        FilmGenre result = new FilmGenre();
        result.setFilmId(resultSet.getInt("film_id"));
        result.setGenreId(resultSet.getInt("genre_id"));
        return result;
    }
}
