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
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
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

    private FilmDto fillFilmData(Film film) {
        log.debug(String.format("Ищем жанры фильма %s", film.getName()));
        LinkedHashSet<Genre> genres = filmStorage.findGenresIds(film.getId()).stream()
                .map(genreStorage::findGenre)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        log.debug(String.format("Ищем лайки фильма %s", film.getName()));
        LinkedHashSet<Long> likes = filmStorage.getLikes(film.getId());

        log.debug(String.format("Ищем рейтинг фильма %s", film.getName()));
        Mpa mpa = mpaStorage.findMpa(filmStorage.findRatingId(film.getId()));

        log.debug(String.format("Фильм %s найден!", film.getName()));
        return FilmMapper.mapToFilmDto(film, mpa, genres, likes);
    }

    public FilmDto findFilm(Long filmId) {
        log.debug(String.format("Поиск фильма с ID %d", filmId));

        return fillFilmData(filmStorage.findFilm(filmId));
    }

    public Collection<FilmDto> findAll() {
        log.debug("Получаем записи о всех фильмах");
        return filmStorage.findAll().stream()
                .map(this::fillFilmData)
                .collect(Collectors.toList());
    }


    public Collection<FilmDto> findPopular(Integer count) {
        if (count <= 0) {
            throw new ValidationException("Количество фильмов должно быть больше 0");
        }

        log.debug(String.format("Получаем список из первых %d фильмов по количеству лайков", count));

        return filmStorage.findPopular(count).stream()
                .map(this::fillFilmData)
                .collect(Collectors.toList());
    }

    public void addLike(Long filmId, Long userId) {
        log.trace("Попытка пользователя поставить лайк фильму...");

        Film film = filmStorage.findFilm(filmId);
        User user = userStorage.findUser(userId);

        filmStorage.addLike(film, user);

        log.debug(String.format("Пользователь %s ставит лайк фильму %s", user.getName(), film.getName()));
    }

    public void deleteLike(Long filmId, Long userId) {
        log.trace("Попытка удалить лайк с фильма...");

        Film film = filmStorage.findFilm(filmId);
        User user = userStorage.findUser(userId);

        filmStorage.deleteLike(film, user);

        log.debug(String.format("Пользователь %s убирает лайк с фильма %s", user.getName(), film.getName()));
    }

    public FilmDto create(NewFilmRequest request) {
        log.debug(String.format("Создаем запись о фильме %s", request.getName()));
        Film film = FilmMapper.mapToFilm(request);

        if (film.getMpa().getId() != null) {
            Mpa mpa = mpaStorage.findMpa(film.getMpa());
        }

        Film createdfilm = filmStorage.create(film);

        Collection<Genre> genres = film.getGenres().stream()
                .map(genreStorage::findGenre)
                .peek(genre -> filmStorage.addGenreId(genre, createdfilm))
                .toList();

        log.trace(String.format("Фильм %s сохранен!", createdfilm.getName()));
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

    public Collection<FilmDto> findCommonFilms(Long userId, Long friendId) {
        log.debug("Получаем список общих фильмов пользователей с сортировкой по популярности");

        User user = userStorage.findUser(userId);
        User friend = userStorage.findUser(friendId);

        Collection<Film> filmsUser = filmStorage.findUserFilms(user.getId());
        Collection<Film> filmsFriend = filmStorage.findUserFilms(friend.getId());

        filmsUser.retainAll(filmsFriend);

        return filmsUser.stream()
                .map(this::fillFilmData)
                .sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .collect(Collectors.toList());
    }
}
