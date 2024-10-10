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
    public Review findReview(Long reviewId) {
        return findOne(ReviewQueries.FIND_BY_ID_QUERY.toString(), baseMapper, reviewId)
                .orElseThrow(() -> new NotFoundException(String.format("Отзыв c ID %d не найден", reviewId)));
    }

    @Override
    public Collection<Review> findAll() {
        return findMany(ReviewQueries.FIND_ALL_QUERY.toString(), baseMapper);
    }

    @Override
    public Collection<Review> reviewsByFilmId(Long film_id, Integer count) {
        log.debug(String.format("Получаем список из %d отзывов фильма", count));

        if (film_id == 0L) {
            return findMany(ReviewQueries.FIND_N_REVIEWS_QUERY.toString(), baseMapper, count);
        }

        return findMany(ReviewQueries.FIND_REVIEWS_BY_FILM_QUERY.toString(), baseMapper, film_id, count);
    }

    @Override
    public void addLike(Review review, User user, boolean isPositive) {
        String errMsg = String.format("Пользователь %s не смог поставить лайк отзыву №%s", user.getName(), review.getId());

        try {
            update(ReviewQueries.INSERT_LIKE_QUERY.toString(), errMsg, review.getId(), user.getId(), isPositive);
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
    public void deleteLike(Review review, User user, boolean isPositive) {
        try {
            delete(ReviewQueries.DELETE_LIKE_QUERY.toString(), review.getId(), user.getId());
        } catch (SQLWarningException e) {
            throw new NotFoundException(String.format("Пользователь %s ещё не ставил лайк отзыву №%d. Удаление не возможно. %s",
                    user.getName(), review.getId(), e.getSQLWarning()));
        }
    }

    @Override
    public Review create(Review review) {
        long id = insert(ReviewQueries.INSERT_REVIEW_QUERY.toString(), review.getUser_id(), review.getFilm_id(),
                review.getContent(), review.isPositive(), review.getUseful());
        review.setId(id);
        return review;
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
