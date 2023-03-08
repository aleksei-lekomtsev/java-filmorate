package ru.yandex.practicum.filmorate.util;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidatorTest {

    @Test
    void shouldThrowValidationExceptionFilmCreateFailReleaseDate() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1890, Month.MARCH, 25));
        film.setDuration(200);

        final RuntimeException exception = assertThrows(ValidationException.class, () -> Validator.validateFilm(film));

        assertEquals("Дата релиза должна быть не раньше 28 декабря 1895 года.", exception.getMessage());
    }
}
