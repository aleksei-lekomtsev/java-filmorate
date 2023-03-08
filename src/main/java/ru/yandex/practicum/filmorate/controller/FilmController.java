package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ru.yandex.practicum.filmorate.util.Validator.validateFilm;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private static Integer            id    = 0;
    private final  Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.debug("Получен запрос POST /films");

        validateFilm(film);

        film.setId(++id);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        log.debug("Получен запрос PUT /films");

        validateFilm(film);

        if(!films.containsKey(film.getId())) {
            throw new ValidationException("Фильм с id: " + film.getId() + " не найден.");
        }

        films.put(film.getId(), film);
        return film;
    }
}
