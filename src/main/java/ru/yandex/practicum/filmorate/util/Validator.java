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
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Название не может быть пустым.");
            throw new ValidationException("Название не может быть пустым.");
        }

        if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.error("Максимальная длина описания — 200 символов.");
            throw new ValidationException("Максимальная длина описания — 200 символов.");
        }

        LocalDate birthdayOfFirstFilm = LocalDate.of(1895, Month.DECEMBER, 28);
        if (film.getReleaseDate().isBefore(birthdayOfFirstFilm)) {
            log.error("Дата релиза должна быть не раньше 28 декабря 1895 года.");
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года.");
        }

        if (film.getDuration() <= 0) {
            log.error("Продолжительность фильма должна быть положительной.");
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
    }

    public static void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.error("Адрес электронной почты не может быть пустым.");
            throw new ValidationException("Адрес электронной почты не может быть пустым.");
        }

        if (!user.getEmail().contains("@")) {
            log.error("Адрес электронной почты должен содержать символ @.");
            throw new ValidationException("Адрес электронной почты должен содержать символ @.");
        }

        if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.error("Логин не может быть пустым и содержать пробелы.");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }

        if (user.getName() == null) {
            user.setName(user.getLogin());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть в будущем.");
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
    }
}
