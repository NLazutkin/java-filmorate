package ru.yandex.practicum.filmorate.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FilmServiceTest {

    private FilmService filmService;
    private FilmStorage filmStorage;
    private UserStorage userStorage;
    private MpaStorage mpaStorage;
    private GenreStorage genreStorage;

    @BeforeEach
    public void setUp() {
        filmStorage = mock(FilmStorage.class);
        userStorage = mock(UserStorage.class);
        mpaStorage = mock(MpaStorage.class);
        genreStorage = mock(GenreStorage.class);
        filmService = new FilmService(filmStorage, userStorage, mpaStorage, genreStorage);
    }

    @Test
    public void testSearchFilmsByTitle() {
        // Arrange
        String query = "крад";
        String by = "title";

        Film film1 = new Film();
        film1.setId(1L);
        film1.setName("Крадущийся тигр, затаившийся дракон");
        film1.setMpa(new Mpa(1L, "G", "General audiences"));

        Film film2 = new Film();
        film2.setId(2L);
        film2.setName("Крадущийся в ночи");
        film2.setMpa(new Mpa(1L, "G", "General audiences"));

        Collection<Film> films = Arrays.asList(film1, film2);

        when(filmStorage.searchFilms(eq(query), eq(true), eq(false))).thenReturn(films);
        when(filmStorage.findRatingId(anyLong())).thenReturn(1L);
        when(mpaStorage.findMpa(anyLong())).thenReturn(new Mpa(1L, "G", "General audiences"));
        when(filmStorage.findGenresIds(anyLong())).thenReturn(new LinkedHashSet<>());
        when(filmStorage.getLikes(anyLong())).thenReturn(new LinkedHashSet<>());

        // Act
        Collection<FilmDto> result = filmService.searchFilms(query, by);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        List<String> filmNames = new ArrayList<>();
        for (FilmDto filmDto : result) {
            filmNames.add(filmDto.getName());
        }

        assertTrue(filmNames.contains("Крадущийся тигр, затаившийся дракон"));
        assertTrue(filmNames.contains("Крадущийся в ночи"));
    }

    @Test
    public void testSearchFilmsByDirector() {
        // Arrange
        String query = "спилберг";
        String by = "director";

        Film film1 = new Film();
        film1.setId(1L);
        film1.setName("Парк Юрского периода");
        film1.setMpa(new Mpa(2L, "PG", "Parental Guidance Suggested"));

        Film film2 = new Film();
        film2.setId(2L);
        film2.setName("Индиана Джонс");
        film2.setMpa(new Mpa(2L, "PG", "Parental Guidance Suggested"));

        Collection<Film> films = Arrays.asList(film1, film2);

        when(filmStorage.searchFilms(eq(query), eq(false), eq(true))).thenReturn(films);
        when(filmStorage.findRatingId(anyLong())).thenReturn(2L);
        when(mpaStorage.findMpa(anyLong())).thenReturn(new Mpa(2L, "PG", "Parental Guidance Suggested"));
        when(filmStorage.findGenresIds(anyLong())).thenReturn(new LinkedHashSet<>());
        when(filmStorage.getLikes(anyLong())).thenReturn(new LinkedHashSet<>());

        // Act
        Collection<FilmDto> result = filmService.searchFilms(query, by);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        List<String> filmNames = new ArrayList<>();
        for (FilmDto filmDto : result) {
            filmNames.add(filmDto.getName());
        }

        assertTrue(filmNames.contains("Парк Юрского периода"));
        assertTrue(filmNames.contains("Индиана Джонс"));
    }

    @Test
    public void testSearchFilmsByTitleAndDirector() {
        // Arrange
        String query = "крад";
        String by = "title,director";

        Film film1 = new Film();
        film1.setId(1L);
        film1.setName("Крадущийся тигр, затаившийся дракон");
        film1.setMpa(new Mpa(1L, "G", "General audiences"));

        Film film2 = new Film();
        film2.setId(2L);
        film2.setName("Фильм режиссера Крадова");
        film2.setMpa(new Mpa(1L, "G", "General audiences"));

        Collection<Film> films = Arrays.asList(film1, film2);

        when(filmStorage.searchFilms(eq(query), eq(true), eq(true))).thenReturn(films);
        when(filmStorage.findRatingId(anyLong())).thenReturn(1L);
        when(mpaStorage.findMpa(anyLong())).thenReturn(new Mpa(1L, "G", "General audiences"));
        when(filmStorage.findGenresIds(anyLong())).thenReturn(new LinkedHashSet<>());
        when(filmStorage.getLikes(anyLong())).thenReturn(new LinkedHashSet<>());

        // Act
        Collection<FilmDto> result = filmService.searchFilms(query, by);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        List<String> filmNames = new ArrayList<>();
        for (FilmDto filmDto : result) {
            filmNames.add(filmDto.getName());
        }

        assertTrue(filmNames.contains("Крадущийся тигр, затаившийся дракон"));
        assertTrue(filmNames.contains("Фильм режиссера Крадова"));
    }

    @Test
    public void testSearchFilmsWithInvalidByParameter() {
        // Arrange
        String query = "some query";
        String by = "invalid";

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            filmService.searchFilms(query, by);
        });

        assertEquals("Недопустимое значение параметра 'by': invalid", exception.getMessage());
    }

    @Test
    public void testSearchFilmsWithEmptyQuery() {
        // Arrange
        String query = "";
        String by = "title";

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            filmService.searchFilms(query, by);
        });

        assertEquals("Параметр 'query' не должен быть пустым", exception.getMessage());
    }

    @Test
    public void testSearchFilmsWithEmptyBy() {
        // Arrange
        String query = "some query";
        String by = "";

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            filmService.searchFilms(query, by);
        });

        assertEquals("Параметр 'by' не должен быть пустым", exception.getMessage());
    }

    @Test
    public void testSearchFilmsWithNoValidByParameters() {
        // Arrange
        String query = "some query";
        String by = "unknown1,unknown2";

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            filmService.searchFilms(query, by);
        });

        assertEquals("Недопустимое значение параметра 'by': unknown1", exception.getMessage());
    }
}
