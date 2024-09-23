package ru.yandex.practicum.filmorate.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FilmService {
    FilmStorage filmStorage;
    UserStorage userStorage;
    MpaStorage mpaStorage;
    GenreStorage genreStorage;

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage"/*"InMemoryFilmStorage"*/) FilmStorage filmStorage,
                       @Qualifier("UserDbStorage"/*"InMemoryUserStorage"*/) UserStorage userStorage,
                       @Qualifier("MpaDbStorage"/*"InMemoryMpaStorage"*/) MpaStorage mpaStorage,
                       @Qualifier("GenreDbStorage"/*"InMemoryGenreStorage"*/) GenreStorage genreStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
    }

    public FilmDto findFilm(Long filmId) {
        log.debug("Поиск фильма с ID " + filmId);

        Film film = filmStorage.findFilm(filmId);

        log.debug("Ищем жанры фильма " + film.getName());
        LinkedHashSet<Genre> genres = filmStorage.findGenresIds(filmId).stream()
                .map(genreStorage::findGenre)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        log.debug("Ищем лайки фильма " + film.getName());
        LinkedHashSet<Long> likes = filmStorage.getLikes(film);

        log.debug("Ищем рейтинг фильма " + film.getName());
        Mpa mpa = mpaStorage.findMpa(filmStorage.findRatingId(filmId));

        log.debug("Фильм " + film.getName() + " найден!");
        return FilmMapper.mapToFilmDto(film, mpa, genres, likes);
    }

    public void addLike(Long filmId, Long userId) {
        log.trace("Попытка пользователя поставить лайк фильму...");

        Film film = filmStorage.findFilm(filmId);
        User user = userStorage.findUser(userId);

        filmStorage.addLike(film, user);

        log.debug("Пользователь " + user.getName() + " ставит лайк фильму " + film.getName());
    }

    public void deleteLike(Long filmId, Long userId) {
        log.trace("Попытка удалить лайк с фильма...");

        Film film = filmStorage.findFilm(filmId);
        User user = userStorage.findUser(userId);

        filmStorage.deleteLike(film, user);

        log.debug("Пользователь " + user.getName() + " убирает лайк с фильма " + film.getName());
    }

    public Collection<FilmDto> findPopular(Integer count) {
        if (count <= 0) {
            throw new ValidationException("Количество фильмов должно быть больше 0");
        }

        log.debug("Получаем список из первых " + count + " фильмов по количеству лайков");

        return filmStorage.findPopular(count).stream().map(FilmMapper::mapToFilmDto).collect(Collectors.toList());
    }

    public Collection<FilmDto> findAll() {
        log.debug("Получаем записи о всех фильмах");
        return filmStorage.findAll().stream().map(FilmMapper::mapToFilmDto).collect(Collectors.toList());
    }

    public FilmDto create(NewFilmRequest request) {
        log.debug("Создаем запись о фильме " + request.getName());
        Film film = FilmMapper.mapToFilm(request);

        if (film.getMpa().getId() != null) {
            Mpa mpa = mpaStorage.findMpa(film.getMpa());
        }

        Film createdfilm = filmStorage.create(film);

        Collection<Genre> genres = film.getGenres().stream()
                .map(genreStorage::findGenre)
                .peek(genre -> filmStorage.addGenreId(genre, createdfilm))
                .toList();

        log.trace("Фильм " + createdfilm.getName() + " сохранен!");
        return FilmMapper.mapToFilmDto(createdfilm);
    }

    public FilmDto update(UpdateFilmRequest request) {
        log.debug("Обновляем данные фильма");

        if (request.getId() == null) {
            throw new ValidationException("Не указан Id фильма!");
        }

        Film updatedFilm = FilmMapper.updateFilmFields(filmStorage.findFilm(request.getId()), request);
        updatedFilm = filmStorage.update(updatedFilm);

        return FilmMapper.mapToFilmDto(updatedFilm);
    }

    public boolean delete(Long filmId) {
        Film film = filmStorage.findFilm(filmId);
        log.debug("Удаляем данные фильма " + film.getName());
        return filmStorage.delete(filmId);
    }
}
