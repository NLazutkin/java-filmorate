package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.LinkedHashSet;

public interface FilmStorage {
    Film findFilm(Long filmId);

    Film update(Film newFilm);

    Film create(Film film);

    Collection<Film> findAll();

    Collection<Film> findPopular(Integer count);

    Collection<Film> findDirectorFilms(Long directorId);

    Collection<Film> findDirectorFilmsOrderYear(Long directorId);

    Collection<Film> findDirectorFilmsOrderLikes(Long directorId);

    boolean delete(Long filmId);

    void addLike(Film film, User user);

    void deleteLike(Film film, User user);

    void addGenreId(Long genreId, Long filmId);

    void addDirectorId(Long directorId, Long filmId);

    boolean deleteGenreIds(Long filmId);

    boolean deleteDirectorIds(Long filmId);

    boolean deleteGenreIds(Long filmId, Long genreId);

    boolean deleteDirectorIds(Long filmId, Long directorId);

    LinkedHashSet<Long> findGenresIds(Long filmId);

    LinkedHashSet<Long> findDirectorsIds(Long filmId);

    Long findRatingId(Long filmId);

    LinkedHashSet<Long> getLikes(Long filmId);

    Collection<Film> getRecommendedFilms(Long userId);

    Collection<Film> findUserFilms(Long userId);

    Collection<Film> findPopularByYear(Integer count, Integer year);

    Collection<Film> findPopularByGenre(Integer count, Long genreId);

    Collection<Film> findPopularByGenreAndYear(Integer count, Long genreId, Integer year);

    Collection<Film> searchFilms(String query, boolean byTitle, boolean byDirector);
}