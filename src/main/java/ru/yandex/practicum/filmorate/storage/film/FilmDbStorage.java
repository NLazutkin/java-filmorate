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
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;
import ru.yandex.practicum.filmorate.storage.mappers.film.FilmBaseRowMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

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
        return findMany(FilmQueries.FIND_POPULAR_QUERY.toString(), baseMapper, count);
    }

    @Override
    public Collection<Film> findPopularByYear(Integer count, Integer year) {
        return findMany(FilmQueries.FIND_POPULAR_BY_YEAR_QUERY.toString(), baseMapper, year, count);
    }

    @Override
    public Collection<Film> findPopularByGenre(Integer count, Long genreId) {
        return findMany(FilmQueries.FIND_POPULAR_BY_GENRE_QUERY.toString(), baseMapper, genreId, count);
    }

    @Override
    public Collection<Film> findPopularByGenreAndYear(Integer count, Long genreId, Integer year) {
        return findMany(FilmQueries.FIND_POPULAR_BY_GENRE_AND_YEAR_QUERY.toString(), baseMapper, genreId, year, count);
    }

    @Override
    public Collection<Film> findDirectorFilms(Long directorId) {
        return findMany(FilmQueries.FIND_DIRECTOR_FILMS_QUERY.toString(), baseMapper, directorId);
    }

    @Override
    public Collection<Film> findDirectorFilmsOrderYear(Long directorId) {
        return findMany(FilmQueries.FIND_DIRECTOR_FILMS_ORDER_YEAR_QUERY.toString(), baseMapper, directorId);
    }

    @Override
    public Collection<Film> findDirectorFilmsOrderLikes(Long directorId) {
        return findMany(FilmQueries.FIND_DIRECTOR_FILMS_ORDER_LIKES_QUERY.toString(), baseMapper, directorId);
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

    public LinkedHashSet<Long> getLikes(Long filmId) {
        return new LinkedHashSet<>(jdbc.query(FilmQueries.FIND_LIKES_BY_ID_QUERY.toString(),
                (rs, rowNum) -> rs.getLong("user_id"), filmId));
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
    public void addDirectorId(Director director, Film film) {
        String errMsg = String.format("Для фильма %s не удалось установить режиссера %s", film.getName(), director.getName());

        try {
            update(FilmQueries.INSERT_FILM_DIRECTOR_QUERY.toString(), errMsg, film.getId(), director.getId());
        } catch (SQLWarningException e) {
            throw new DuplicatedDataException(String.format("Для фильма %s режиссер %s уже установлен. %s",
                    film.getName(), director.getName(), e.getSQLWarning()));
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
    public LinkedHashSet<Long> findDirectorsIds(Long filmId) {
        return new LinkedHashSet<>(jdbc.query(FilmQueries.FIND_DIRECTOR_ID_QUERY.toString(),
                (rs, rowNum) -> rs.getLong("director_id"), filmId));
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

    @Override
    public Collection<Film> searchFilms(String query, boolean byTitle, boolean byDirector) {
        String lowerQuery = "%" + query.toLowerCase() + "%";
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT f.*, COUNT(l.user_id) AS popularity " +
                        "FROM films f " +
                        "LEFT JOIN likes l ON f.id = l.film_id "
        );

        if (byDirector) {
            sql.append(
                    "LEFT JOIN films_directors fd ON f.id = fd.film_id " +
                            "LEFT JOIN directors d ON fd.director_id = d.id "
            );
        }

        sql.append("WHERE ");

        if (byTitle && byDirector) {
            sql.append("(LOWER(f.name) LIKE ? OR LOWER(d.name) LIKE ?) ");
            params.add(lowerQuery);
            params.add(lowerQuery);
        } else if (byTitle) {
            sql.append("LOWER(f.name) LIKE ? ");
            params.add(lowerQuery);
        } else if (byDirector) {
            sql.append("LOWER(d.name) LIKE ? ");
            params.add(lowerQuery);
        } else {
            sql.append("LOWER(f.name) LIKE ? ");
            params.add(lowerQuery);
        }

        sql.append(
                "GROUP BY f.id " +
                        "ORDER BY popularity DESC"
        );

        String finalSql = sql.toString();
        log.debug("SQL запрос для поиска фильмов: {}", finalSql);

        return findMany(finalSql, baseMapper, params.toArray());
    }

    public Collection<Film> findUserFilms(Long userId) {
        return findMany(FilmQueries.FIND_USER_FILMS_QUERY.toString(), baseMapper, userId);
    }

    @Override
    public Collection<Film> getRecommendedFilms(Long userId) {
        Collection<Long> likedFilms = getFilmsLikedByUser(userId);
        Long similarUser = findMostSimilarUser(userId, likedFilms);

        if (similarUser == null) {
            return Collections.emptyList();
        }

        Collection<Long> recommendedFilmsIds = getFilmsLikedByUser(similarUser);
        recommendedFilmsIds.removeAll(likedFilms);

        return recommendedFilmsIds.stream()
                .map(this::findFilm)
                .collect(Collectors.toList());
    }

    private Collection<Long> getFilmsLikedByUser(Long userId) {
        return new HashSet<>(jdbc.queryForList(FilmQueries.FIND_LIKED_FILMS_BY_USER_ID_QUERY.toString(),
                Long.class, userId));
    }

    private Long findMostSimilarUser(Long userId, Collection<Long> filmsLikedByUser) {
        return jdbc.query(FilmQueries.FIND_MOST_SIMILAR_USER.toString(), rs -> {
            if (rs.next()) {
                return rs.getLong("user_id");
            } else {
                return null;
            }
        }, userId, userId);
    }
}
