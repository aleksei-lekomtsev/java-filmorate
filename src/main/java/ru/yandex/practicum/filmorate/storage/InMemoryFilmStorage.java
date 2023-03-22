package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private static Integer            id    = 0;
    private final      Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film createFilm(Film film) {
        if (film == null) {
            log.warn("Произошла непредвиденная ошибка. Значение film не может быть null");
            throw new RuntimeException("Произошла непредвиденная ошибка. Значение film не может быть null");
        }
        film.setId(++id);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (film == null) {
            log.warn("Произошла непредвиденная ошибка. Значение film не может быть null");
            throw new RuntimeException("Произошла непредвиденная ошибка. Значение film не может быть null");
        }
        if(!films.containsKey(film.getId())) {
            log.warn("Фильм с id: " + film.getId() + " не найден.");
            throw new FilmNotFoundException("Фильм с id: " + film.getId() + " не найден.");
        }

        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film findFilmById(Integer id) {
        if(!films.containsKey(id)) {
            throw new FilmNotFoundException("Фильм с id: " + id + " не найден.");
        }
        return films.get(id);
    }
}
