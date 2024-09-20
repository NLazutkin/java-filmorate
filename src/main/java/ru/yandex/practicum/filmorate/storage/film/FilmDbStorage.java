package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.SQLWarningException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.enums.query.FilmQueries;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;
import ru.yandex.practicum.filmorate.storage.mappers.film.FilmBaseRowMapper;
import ru.yandex.practicum.filmorate.storage.mappers.genre.GenreBaseRowMapper;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

@Slf4j
@Component("FilmDbStorage")
public class FilmDbStorage extends BaseDbStorage<Film> implements FilmStorage {
    protected final RowMapper<Film> baseMapper;
    protected final RowMapper<Genre> genreBaseMapper;
    protected final RowMapper<Mpa> mpaBaseMapper;

    public FilmDbStorage(JdbcTemplate jdbc, FilmBaseRowMapper baseMapper, GenreBaseRowMapper genreBaseMapper,
                         RowMapper<Mpa> mpaBaseMapper) {
        super(jdbc);
        this.baseMapper = baseMapper;
        this.genreBaseMapper = genreBaseMapper;
        this.mpaBaseMapper = mpaBaseMapper;
    }

    @Override
    public Mpa findRatingById(Long filmId) {
        try {
            return jdbc.queryForObject(FilmQueries.FIND_FILM_QUERY.toString(), mpaBaseMapper, filmId);
        } catch (EmptyResultDataAccessException ignored) {
            return new Mpa();
        }
    }

    @Override
    public Set<Genre> findGenresById(Long filmId) {
        return new LinkedHashSet<>(jdbc.query(FilmQueries.FIND_GENRES_BY_FILM_ID_QUERY.toString(), genreBaseMapper, filmId));
    }

    @Override
    public Film findFilm(Long filmId) {
        return findOne(FilmQueries.FIND_BY_ID_QUERY.toString(), baseMapper, filmId)
                .orElseThrow(() -> new NotFoundException("Фильм c ID " + filmId + " не найден"));
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
        String errMsg = "Пользователь " + user.getName() + "не смог поставить лайк фильму" + film.getName();

        try {
            update(FilmQueries.INSERT_LIKE_QUERY.toString(), errMsg, film.getId(), user.getId());
        } catch (SQLWarningException e) {
            throw new DuplicatedDataException("Пользователь " + user.getName() + " уже ставил лайк фильму "
                    + film.getName() + ". " + e.getSQLWarning());
        }
    }

    @Override
    public void addGenre(Genre genre, Film film) {
        String errMsg = "Для фильма " + film.getName() + "не удалось установить жанр " + genre.getName();

        try {
            update(FilmQueries.INSERT_FILM_GENRE_QUERY.toString(), errMsg, film.getId(), genre.getId());
        } catch (SQLWarningException e) {
            throw new DuplicatedDataException("Для фильма " + film.getName() + " жанр " + genre.getName()
                    + " уже установлен. " + e.getSQLWarning());
        }
    }

    @Override
    public void deleteLike(Film film, User user) {
        try {
            delete(FilmQueries.DELETE_LIKE_QUERY.toString(), film.getId(), user.getId());
        } catch (SQLWarningException e) {
            throw new NotFoundException("Пользователь " + user.getName() + " ещё не ставил лайк фильму "
                    + film.getName() + ". Удаление не возможно. " + e.getSQLWarning());
        }
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
