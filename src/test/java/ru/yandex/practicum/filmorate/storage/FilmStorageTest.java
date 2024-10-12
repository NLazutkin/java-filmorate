package ru.yandex.practicum.filmorate.storage;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;

import java.util.Collection;

import java.util.LinkedHashSet;
import java.util.Objects;

@JdbcTest
@AutoConfigureTestDatabase
@ComponentScan("ru.yandex.practicum.filmorate")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmStorageTest {
    FilmDbStorage filmStorage;
    UserDbStorage userStorage;

    @Test
    public void testFindFilmById() {
        assertThat(filmStorage.findFilm(1L))
                .isInstanceOf(Film.class)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "Тень");
    }

    @Test
    public void testFindAll() {
        assertThat(filmStorage.findAll()).isNotEmpty()
                .hasSize(4)
                .filteredOn("name", "Тень")
                .isNotEmpty()
                .hasExactlyElementsOfTypes(Film.class);
    }

    @Test
    public void testFindPopular() {
        final int count = 4;
        Collection<Film> films = filmStorage.findPopular(count);

        assertThat(films).isNotEmpty()
                .hasSize(count)
                .isInstanceOf(Collection.class)
                .first()
                .extracting(Film::getId)
                .isEqualTo(2L);
    }

    @Test
    public void testAddLike() {
        Film film = new Film();
        film.setId(3L);
        film.setName("Зеленая миля");

        User user = new User();
        user.setId(3L);
        user.setEmail("Sparrow@yandex.ru");
        user.setName("Sparrow");

        assertThat(filmStorage.getLikes(film.getId()))
                .isNotEmpty()
                .isInstanceOf(LinkedHashSet.class)
                .hasSize(1)
                .containsOnly(2L);

        filmStorage.addLike(film, user);

        assertThat(filmStorage.getLikes(film.getId()))
                .isNotEmpty()
                .isInstanceOf(LinkedHashSet.class)
                .hasSize(2)
                .containsOnly(2L, 3L);
    }

    @Test
    public void testGetLikes() {
        Film film = new Film();
        film.setId(4L);
        film.setName("Гадкий я");

        assertThat(filmStorage.getLikes(film.getId()))
                .isNotEmpty()
                .isInstanceOf(LinkedHashSet.class)
                .hasSize(3)
                .containsOnly(1L, 2L, 3L);
    }

    @Test
    public void testDeleteLike() {
        Film film = new Film();
        film.setId(4L);
        film.setName("Гадкий я");

        User user = new User();
        user.setId(3L);
        user.setEmail("Sparrow@yandex.ru");
        user.setName("Sparrow");

        assertThat(filmStorage.getLikes(film.getId()))
                .isNotEmpty()
                .isInstanceOf(LinkedHashSet.class)
                .hasSize(3)
                .containsOnly(1L, 2L, 3L);

        filmStorage.deleteLike(film, user);

        assertThat(filmStorage.getLikes(film.getId()))
                .isNotEmpty()
                .isInstanceOf(LinkedHashSet.class)
                .hasSize(2)
                .containsOnly(1L, 2L);
    }

    @Test
    public void testFindRatingId() {
        assertThat(filmStorage.findRatingId(2L))
                .isNotNull()
                .isInstanceOf(Long.class)
                .isEqualTo(2L);
    }

    @Test
    public void testAddGenreId() {
        assertThat(filmStorage.findGenresIds(4L))
                .isNotEmpty()
                .isInstanceOf(LinkedHashSet.class)
                .hasSize(4)
                .containsOnly(1L, 3L, 4L, 6L);

        Film film = new Film();
        film.setId(4L);
        film.setName("Гадкий я");

        Genre genre = new Genre();
        genre.setId(2L);
        genre.setName("Драма");

        filmStorage.addGenreId(genre, film);

        assertThat(filmStorage.findGenresIds(4L))
                .isNotEmpty()
                .isInstanceOf(LinkedHashSet.class)
                .hasSize(5)
                .containsOnly(1L, 2L, 3L, 4L, 6L);
    }

    @Test
    public void testFindGenresIds() {
        assertThat(filmStorage.findGenresIds(2L))
                .isNotEmpty()
                .isInstanceOf(LinkedHashSet.class)
                .hasSize(3)
                .containsOnly(2L, 4L, 6L);
    }

    @Test
    public void testCreateFilm() {
        Film newFilm = new Film();
        newFilm.setName("Американский ниндзя");
        newFilm.setDescription("Джо Армстронг, 18-летний рядовой филиппинского отряда армии США, ...");
        newFilm.setReleaseDate(LocalDate.of(1985, 8, 30));
        newFilm.setDuration(95L);
        newFilm.setMpa(new Mpa());

        assertThat(filmStorage.create(newFilm))
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 5L);
    }

    @Test
    public void testUpdateFilm() {
        Film newFilm = new Film();
        newFilm.setId(4L);
        newFilm.setName("Гадкий я");
        newFilm.setDescription("Гадкий снаружи, но добрый внутри Грю намерен, тем не менее, ...");
        newFilm.setReleaseDate(LocalDate.of(2000, 1, 1));
        newFilm.setDuration(95L);

        assertThat(filmStorage.update(newFilm))
                .isNotNull()
                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2000, 1, 1));
    }

    @Test
    public void testDeleteFilm() {
        assertThat(filmStorage.delete(4L)).isTrue();
    }

    @Test
    public void testFindRecommendedFilm() {
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("user1@yandex.ru");
        user1.setName("User1 Name");
        user1.setLogin("User1Login");
        user1.setBirthday(LocalDate.of(2000, 1, 1));
        userStorage.create(user1);

        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("user2@yandex.ru");
        user2.setName("User2 Name");
        user2.setLogin("User2Login");
        user2.setBirthday(LocalDate.of(2000, 1, 1));
        userStorage.create(user2);

        Film newFilm = new Film();
        newFilm.setId(5L);
        newFilm.setName("Бойцовский клуб");
        newFilm.setDescription("Сотрудник страховой компании страдает хронической бессонницей" +
                " и отчаянно пытается вырваться из мучительно скучной жизни.");
        newFilm.setReleaseDate(LocalDate.of(2000, 1, 1));
        newFilm.setDuration(120L);
        newFilm.setMpa(new Mpa());
        filmStorage.create(newFilm);

        Film newFilm2 = new Film();
        newFilm2.setId(6L);
        newFilm2.setName("Большой куш");
        newFilm2.setDescription("Фильм о группе криминальных отщепенцев, которые оказываются втянутыми в череду" +
                " странных и зачастую уморительных событий.");
        newFilm2.setReleaseDate(LocalDate.of(2000, 1, 1));
        newFilm2.setDuration(120L);
        newFilm2.setMpa(new Mpa());
        filmStorage.create(newFilm2);

        //user1 - 1 лайк
        filmStorage.addLike(newFilm, user1);

        //user2 - 2 лайка
        filmStorage.addLike(newFilm, user2);
        filmStorage.addLike(newFilm2, user2);

        // recommendedFilms - 1 фильм
        Collection<Film> recommendedFilms = filmStorage.getRecommendedFilms(user1.getId());
        assertThat(recommendedFilms)
                .hasSize(1)
                .extracting(Film::getName)
                .containsOnly("Большой куш");
    }

    @Test
    public void testFindUserFilms() {
        User user = new User();
        user.setId(3L);

        assertThat(filmStorage.findUserFilms(user.getId())).isNotEmpty()
                .hasSize(3)
                .isInstanceOf(Collection.class);
    }

    @Test
    public void testFindPopularByYear() {
        Integer count = 10;
        Integer year = 2010;

        assertThat(filmStorage.findPopularByYear(count, year)).isNotEmpty()
                .hasSize(1)
                .isInstanceOf(Collection.class)
                .allMatch(film -> film.getReleaseDate().getYear() == 2010);
    }

    @Test
    public void testFindPopularByGenre() {
        Integer count = 10;
        Long genreId = 1L;

        assertThat(filmStorage.findPopularByGenre(count, genreId)).isNotEmpty()
                .hasSize(2)
                .isInstanceOf(Collection.class)
                .allMatch(film -> film.getGenres().stream().allMatch(genre -> Objects.equals(genre.getId(), genreId)));
    }

    @Test
    public void testFindPopularByGenreAndYear() {
        Integer count = 10;
        Long genreId = 3L;
        Integer year = 2010;

        assertThat(filmStorage.findPopularByGenreAndYear(count, genreId,year)).isNotEmpty()
                .hasSize(1)
                .isInstanceOf(Collection.class)
                .allMatch(film -> film.getReleaseDate().getYear() == 2010)
                .allMatch(film -> film.getGenres().stream()
                        .allMatch(genre -> Objects.equals(genre.getId(), genreId)));
    }
}