package ru.yandex.practicum.filmorate.storage.review;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.SQLWarningException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.enums.query.ReviewQueries;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;
import ru.yandex.practicum.filmorate.storage.mappers.review.ReviewBaseRowMapper;

import java.util.Collection;
import java.util.LinkedHashSet;

@Slf4j
@Component("ReviewDbStorage")
@FieldDefaults(makeFinal = true, level = AccessLevel.PROTECTED)
public class ReviewDbStorage extends BaseDbStorage<Review> implements ReviewStorage {
    RowMapper<Review> baseMapper;

    @Autowired
    public ReviewDbStorage(JdbcTemplate jdbc, ReviewBaseRowMapper baseMapper) {
        super(jdbc);
        this.baseMapper = baseMapper;
    }

    @Override
    public Film findFilm(Long filmId) {
        return findOne(ReviewQueries.FIND_BY_ID_QUERY.toString(), baseMapper, filmId)
                .orElseThrow(() -> new NotFoundException(String.format("Фильм c ID %d не найден", filmId)));
    }

    @Override
    public Collection<Film> findAll() {
        return findMany(ReviewQueries.FIND_ALL_QUERY.toString(), baseMapper);
    }

    @Override
    public Collection<Film> findPopular(Integer count) {
        return findMany(ReviewQueries.FIND_POPULAR_QUERY.toString(), baseMapper);
    }

    @Override
    public void addLike(Review review, User user) {
        String errMsg = String.format("Пользователь %s не смог поставить лайк отзыву №%s", user.getName(), review.getId());

        try {
            update(ReviewQueries.INSERT_LIKE_QUERY.toString(), errMsg, review.getId(), user.getId());
        } catch (SQLWarningException e) {
            throw new DuplicatedDataException(String.format("Пользователь %s уже ставил лайк отзыву №%d. %s",
                    user.getName(), review.getId(), e.getSQLWarning()));
        }
    }

    @Override
    public LinkedHashSet<Long> getLikes(Long reviewId) {
        return new LinkedHashSet<>(jdbc.query(ReviewQueries.FIND_LIKES_BY_ID_QUERY.toString(),
                (rs, rowNum) -> rs.getLong("user_id"), reviewId));
    }

    @Override
    public void deleteLike(Review review, User user) {
        try {
            delete(ReviewQueries.DELETE_LIKE_QUERY.toString(), review.getId(), user.getId());
        } catch (SQLWarningException e) {
            throw new NotFoundException(String.format("Пользователь %s ещё не ставил лайк отзыву №%d. Удаление не возможно. %s",
                    user.getName(), review.getId(), e.getSQLWarning()));
        }
    }

    @Override
    public Film create(Review review) {
        long id = insert(ReviewQueries.INSERT_REVIEW_QUERY.toString(), review.getUser_id(), review.getFilm_id(),
                review.getContent(), review.isPositive(), review.getUseful());
        film.setId(id);
        return film;
    }

    @Override
    public Review update(Review newReview) {
        update(ReviewQueries.UPDATE_QUERY.toString(), "Не удалось обновить данные отзыва", newReview.getUser_id(),
                newReview.getFilm_id(), newReview.getContent(), newReview.isPositive(), newReview.getUseful(), newReview.getId());
        return newReview;
    }

    @Override
    public boolean delete(Long reviewId) {
        return delete(ReviewQueries.DELETE_QUERY.toString(), reviewId);
    }
}
