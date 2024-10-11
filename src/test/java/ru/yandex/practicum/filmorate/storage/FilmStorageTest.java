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