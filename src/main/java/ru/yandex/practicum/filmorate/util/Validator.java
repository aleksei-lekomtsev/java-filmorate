package ru.yandex.practicum.filmorate.util;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;

@Slf4j
public class Validator {
    public static void validateFilm(Film film) {
        LocalDate birthdayOfFirstFilm = LocalDate.of(1895, Month.DECEMBER, 28);
        if (film.getReleaseDate().isBefore(birthdayOfFirstFilm)) {
            log.error("Дата релиза должна быть не раньше 28 декабря 1895 года.");
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года.");
        }
    }

    public static void validateUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
