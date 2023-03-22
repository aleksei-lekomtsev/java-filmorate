package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film findFilmById(Integer id) {
        return filmStorage.findFilmById(id);
    }

    public Film addLike(Integer id, Integer userId) {
        Film film = filmStorage.findFilmById(id);
        film.getLikes().add(userId);
        return film;
    }

    public Film deleteLike(Integer id, Integer userId) {
        Film film = filmStorage.findFilmById(id);
        if (film.getLikes().contains(userId)) {
            film.getLikes().remove(userId);
        } else {
            log.warn("Пользователь с id: " + userId + " не найден.");
            throw new UserNotFoundException("Пользователь с id: " + userId + " не найден.");
        }
        return film;
    }

    public Collection<Film> findMostLikedFilms(Integer count) {
        Comparator<Film> comparator = Comparator.comparing(film -> film.getLikes().size());
        return filmStorage.findAll()
                          .stream()
                          .sorted(comparator.reversed())
                          .limit(count)
                          .collect(Collectors.toList());
    }
}
