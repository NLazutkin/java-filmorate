package ru.yandex.practicum.filmorate.storage.film;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.SQLWarningException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.enums.query.FilmQueries;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;
import ru.yandex.practicum.filmorate.storage.mappers.film.FilmBaseRowMapper;

import java.util.Collection;
import java.util.LinkedHashSet;

@Slf4j
@Component("FilmDbStorage")
@FieldDefaults(makeFinal = true, level = AccessLevel.PROTECTED)
public class FilmDbStorage extends BaseDbStorage<Film> implements FilmStorage {
    RowMapper<Film> baseMapper;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbc, FilmBaseRowMapper baseMapper) {
        super(jdbc);
        this.baseMapper = baseMapper;
    }

    @Override
    public Film findFilm(Long filmId) {
        return findOne(FilmQueries.FIND_BY_ID_QUERY.toString(), baseMapper, filmId)
                .orElseThrow(() -> new NotFoundException(String.format("Фильм c ID %d не найден", filmId)));
    }

    @Override
    public Collection<Film> findAll() {
        return findMany(FilmQueries.FIND_ALL_QUERY.toString(), baseMapper);
    }

    @Override
    public Collection<Film> findPopular(Integer count) {
        return findMany(FilmQueries.FIND_POPULAR_QUERY.toString(), baseMapper);
    }

    @Override
    public void addLike(Film film, User user) {
        String errMsg = String.format("Пользователь %s не смог поставить лайк фильму %s", user.getName(), film.getName());

        try {
            update(FilmQueries.INSERT_LIKE_QUERY.toString(), errMsg, film.getId(), user.getId());
        } catch (SQLWarningException e) {
            throw new DuplicatedDataException(String.format("Пользователь %s уже ставил лайк фильму %s. %s",
                    user.getName(), film.getName(), e.getSQLWarning()));
        }
    }

    public LinkedHashSet<Long> getLikes(Film film) {
        return new LinkedHashSet<>(jdbc.query(FilmQueries.FIND_LIKES_BY_ID_QUERY.toString(),
                (rs, rowNum) -> rs.getLong("user_id"), film.getId()));
    }

    @Override
    public void deleteLike(Film film, User user) {
        try {
            delete(FilmQueries.DELETE_LIKE_QUERY.toString(), film.getId(), user.getId());
        } catch (SQLWarningException e) {
            throw new NotFoundException(String.format("Пользователь %s ещё не ставил лайк фильму %s. Удаление не возможно. %s",
                    user.getName(), film.getName(), e.getSQLWarning()));
        }
    }

    @Override
    public void addGenreId(Genre genre, Film film) {
        String errMsg = String.format("Для фильма %s не удалось установить жанр %s", film.getName(), genre.getName());

        try {
            update(FilmQueries.INSERT_FILM_GENRE_QUERY.toString(), errMsg, film.getId(), genre.getId());
        } catch (SQLWarningException e) {
            throw new DuplicatedDataException(String.format("Для фильма %s жанр %s уже установлен. %s",
                    film.getName(), genre.getName(), e.getSQLWarning()));
        }
    }

    @Override
    public Long findRatingId(Long filmId) {
        return findId(FilmQueries.FIND_RATING_ID_QUERY.toString(), filmId);
    }

    @Override
    public LinkedHashSet<Long> findGenresIds(Long filmId) {
        return new LinkedHashSet<>(jdbc.query(FilmQueries.FIND_GENRE_ID_QUERY.toString(),
                (rs, rowNum) -> rs.getLong("genre_id"), filmId));
    }

    @Override
    public Film create(Film film) {
        long id = insert(FilmQueries.INSERT_FILM_QUERY.toString(), film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), film.getMpa().getId());
        film.setId(id);
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        update(FilmQueries.UPDATE_QUERY.toString(), "Не удалось обновить данные фильма", newFilm.getName(),
                newFilm.getDescription(), newFilm.getReleaseDate(), newFilm.getDuration(), newFilm.getId());
        return newFilm;
    }

    @Override
    public boolean delete(Long filmId) {
        return delete(FilmQueries.DELETE_QUERY.toString(), filmId);
    }
}
