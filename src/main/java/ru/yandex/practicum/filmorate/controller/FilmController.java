package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;

import static ru.yandex.practicum.filmorate.util.Validator.validateFilm;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    // > А почему в этом методе и findMostLikedFilms не проводишь логирование?
    // Где-то я читал, может в пачке, что логирование вроде как делать в методах, которые изменяют данные.
    // GET метод, если я правильно понимаю, не изменяет данные, поэтому не добавлял лоирование. Сейчас добавил)
    //
    @GetMapping
    public Collection<Film> findAll() {
        log.debug("Получен запрос GET /films");
        return filmService.findAll();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.debug("Получен запрос POST /films");
        validateFilm(film);
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.debug("Получен запрос PUT /films");
        validateFilm(film);
        return filmService.updateFilm(film);
    }

    @GetMapping("/{id}")
    public Film findFilm(@PathVariable("id") Integer id) {
        log.debug("Получен запрос GET /films/{id}");
        return filmService.findFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        log.debug("Получен запрос PUT /films/{id}/like/{userId}");
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        log.debug("Получен запрос DELETE /films/{id}/like/{userId}");
        return filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> findMostLikedFilms(@RequestParam(defaultValue = "10", required = false) Integer count) {
        log.debug("Получен запрос GET /films/popular");
        return filmService.findMostLikedFilms(count);
    }
}
