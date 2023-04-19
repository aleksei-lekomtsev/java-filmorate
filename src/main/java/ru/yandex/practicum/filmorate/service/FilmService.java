package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmGenreDbStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage        filmStorage;
    private final LikeDbStorage      likeDbStorage;
    private final FilmGenreDbStorage filmGenreDbStorage;
    private final GenreDbStorage     genreDbStorage;
    private final MpaDbStorage       mpaDbStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       LikeDbStorage likeDbStorage,
                       FilmGenreDbStorage filmGenreDbStorage,
                       GenreDbStorage genreDbStorage,
                       MpaDbStorage mpaDbStorage) {
        this.filmStorage = filmStorage;
        this.likeDbStorage = likeDbStorage;
        this.filmGenreDbStorage = filmGenreDbStorage;
        this.genreDbStorage = genreDbStorage;
        this.mpaDbStorage = mpaDbStorage;
    }

    private void createFilmGenreFor(Film film) {
        for (Genre genre : film.getGenres()) {
            FilmGenre filmGenre = new FilmGenre();
            filmGenre.setFilmId(film.getId());
            filmGenre.setGenreId(genre.getId());
            filmGenreDbStorage.create(filmGenre);
        }
    }

    private void addGenresFor(Film film) {
        Collection<FilmGenre> filmsGenres = filmGenreDbStorage.findByFilmId(film.getId());
        Collection<Genre> genres = genreDbStorage.findByIds(filmsGenres
                .stream()
                .map(FilmGenre::getGenreId)
                .collect(Collectors.toList()));
        film.getGenres().clear();
        film.getGenres().addAll(genres);
    }

    private void addMpaFor(Film film) {
        Mpa mpa = mpaDbStorage.findById(film.getMpa().getId());
        film.getMpa().setName(mpa.getName());
    }

    public Film createFilm(Film film) {
        film = filmStorage.create(film);
        createFilmGenreFor(film);
        return film;
    }

    public Film updateFilm(Film film) {
        filmGenreDbStorage.delete(film.getId());
        createFilmGenreFor(film);
        addGenresFor(film);
        return filmStorage.update(film);
    }

    public Collection<Film> findAll() {
        Collection<Film> result = filmStorage.findAll();
        for (Film film : result) {
            addGenresFor(film);
            addMpaFor(film);
        }
        return result;
    }

    public Film findFilmById(Integer id) {
        Film result = filmStorage.findById(id);
        addGenresFor(result);
        addMpaFor(result);
        return result;
    }

    public void addLike(Integer id, Integer userId) {
        likeDbStorage.create(id, userId);
    }

    public void deleteLike(Integer id, Integer userId) {
        likeDbStorage.delete(id, userId);
    }

    public Collection<Film> findMostLikedFilms(Integer count) {
        Collection<Integer> mostLikedFilms = likeDbStorage.findMostLikedFilms(count);
        Collection<Film> result = mostLikedFilms.isEmpty()
                ? filmStorage.findAll()
                             .stream()
                             .limit(count)
                             .collect(Collectors.toList())
                : filmStorage.findByIds(mostLikedFilms);
        for (Film film : result) {
            addGenresFor(film);
            addMpaFor(film);
        }
        return result;
    }
}
