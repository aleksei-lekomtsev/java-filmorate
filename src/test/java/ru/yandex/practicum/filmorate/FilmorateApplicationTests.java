package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;


import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
class FilmorateApplicationTests {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final MpaDbStorage   mpaDbStorage;
    private final GenreDbStorage genreDbStorage;

    @Autowired
    public FilmorateApplicationTests(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                                     @Qualifier("userDbStorage") UserStorage userStorage,
                                     GenreDbStorage genreDbStorage,
                                     MpaDbStorage mpaDbStorage
                                     ) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
        this.mpaDbStorage = mpaDbStorage;
        this.genreDbStorage = genreDbStorage;
    }

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setName("name");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(2020, 1, 8));
        user.setEmail("mail@mail.ru");

        userStorage.create(user);

        user = userStorage.findById(1);

        assertThat(user.getId())
                .isEqualTo(1);
    }

    @Test
    public void testUpdateFilm() {
        Film film = new Film();
        film.setName("name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(2020, 1, 8));
        film.setDuration(100);
        film.getMpa().setId(1);

        filmStorage.create(film);

        film.setName("new name");
        filmStorage.update(film);

        film = filmStorage.findById(1);

        assertThat(film.getName())
                .isEqualTo("new name");
    }

    @Test
    public void testFindMpaById() {
        Mpa mpa = mpaDbStorage.findById(1);

        assertThat(mpa.getName())
                .isEqualTo("G");
    }

    @Test
    public void testFindAllMpa() {
        Mpa mpa1 = mpaDbStorage.findById(1);
        Mpa mpa2 = mpaDbStorage.findById(2);
        Mpa mpa3 = mpaDbStorage.findById(3);
        Mpa mpa4 = mpaDbStorage.findById(4);
        Mpa mpa5 = mpaDbStorage.findById(5);

        assertThat(mpa1.getName())
                .isEqualTo("G");

        assertThat(mpa2.getName())
                .isEqualTo("PG");

        assertThat(mpa3.getName())
                .isEqualTo("PG-13");

        assertThat(mpa4.getName())
                .isEqualTo("R");

        assertThat(mpa5.getName())
                .isEqualTo("NC-17");
    }

    @Test
    public void testFindGenreById() {
        Genre genre = genreDbStorage.findById(1);

        assertThat(genre.getName())
                .isEqualTo("Комедия");
    }

    @Test
    public void testFindAllGenre() {
        Genre genre1 = genreDbStorage.findById(1);
        Genre genre2 = genreDbStorage.findById(2);
        Genre genre3 = genreDbStorage.findById(3);
        Genre genre4 = genreDbStorage.findById(4);
        Genre genre5 = genreDbStorage.findById(5);
        Genre genre6 = genreDbStorage.findById(6);

        assertThat(genre1.getName())
                .isEqualTo("Комедия");

        assertThat(genre2.getName())
                .isEqualTo("Драма");

        assertThat(genre3.getName())
                .isEqualTo("Мультфильм");

        assertThat(genre4.getName())
                .isEqualTo("Триллер");

        assertThat(genre5.getName())
                .isEqualTo("Документальный");

        assertThat(genre6.getName())
                .isEqualTo("Боевик");
    }
}
