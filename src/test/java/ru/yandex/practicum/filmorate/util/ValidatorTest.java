package ru.yandex.practicum.filmorate.util;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidatorTest {

    @Test
    void shouldThrowValidationExceptionUserCreateFailLogin() {
        User user = new User();
        user.setName("dolore ullamco");
        user.setEmail("yandex@mail.ru");
        user.setBirthday(LocalDate.of(2446, Month.AUGUST, 20));

        final RuntimeException exception = assertThrows(ValidationException.class, () -> Validator.validateUser(user));

        assertEquals("Логин не может быть пустым и содержать пробелы.", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionUserCreateFailEmail() {
        User user = new User();
        user.setLogin("dolore ullamco");
        user.setName("");
        user.setEmail("mail.ru");
        user.setBirthday(LocalDate.of(1980, Month.AUGUST, 20));

        final RuntimeException exception = assertThrows(ValidationException.class, () -> Validator.validateUser(user));

        assertEquals("Адрес электронной почты должен содержать символ @.", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionUserCreateFailBirthday() {
        User user = new User();
        user.setLogin("dolore");
        user.setName("");
        user.setEmail("test@mail.ru");
        user.setBirthday(LocalDate.of(2446, Month.AUGUST, 20));

        final RuntimeException exception = assertThrows(ValidationException.class, () -> Validator.validateUser(user));

        assertEquals("Дата рождения не может быть в будущем.", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionFilmCreateFailName() {
        Film film = new Film();
        film.setName("");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1900, Month.MARCH, 25));
        film.setDuration(200);

        final RuntimeException exception = assertThrows(ValidationException.class, () -> Validator.validateFilm(film));

        assertEquals("Название не может быть пустым.", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionFilmCreateFailDescription() {
        Film film = new Film();
        film.setName("Film name");
        film.setDescription("\"Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль." +
                " Здесь они хотят разыскать господина Огюста Куглова, который задолжал им деньги," +
                " а именно 20 миллионов. о Куглов, который за время «своего отсутствия», стал кандидатом Коломбани.\"");
        film.setReleaseDate(LocalDate.of(1900, Month.MARCH, 25));
        film.setDuration(200);

        final RuntimeException exception = assertThrows(ValidationException.class, () -> Validator.validateFilm(film));

        assertEquals("Максимальная длина описания — 200 символов.", exception.getMessage());
    }

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

    @Test
    void shouldThrowValidationExceptionFilmCreateFailDuration() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1980, Month.MARCH, 25));
        film.setDuration(-200);

        final RuntimeException exception = assertThrows(ValidationException.class, () -> Validator.validateFilm(film));

        assertEquals("Продолжительность фильма должна быть положительной.", exception.getMessage());
    }
}
