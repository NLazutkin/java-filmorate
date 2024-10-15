package ru.yandex.practicum.filmorate.storage.review;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.enums.query.ReviewQueries;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;
import ru.yandex.practicum.filmorate.storage.mappers.review.ReviewBaseRowMapper;

import java.util.Collection;

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
    public Collection<Review> reviewsByFilmId(Long filmId, Integer count) {
        log.debug("Получаем список из {} отзывов фильма с ID {}", count, filmId);

        if (filmId == 0L) {
            return findMany(ReviewQueries.FIND_N_REVIEWS_QUERY.toString(), baseMapper, count);
        }

        return findMany(ReviewQueries.FIND_REVIEWS_BY_FILM_QUERY.toString(), baseMapper, filmId, count);
    }

    @Override
    public void increaseUseful(Long reviewId) {
        update(ReviewQueries.INCREASE_USEFUL_QUERY.toString(), "Не удалось увеличить рейтинг отзыва при отметке лайком", reviewId);
        log.trace("Рейтинг полезности отзыва увеличен на 1");
    }

    @Override
    public void decreaseUseful(Long reviewId) {
        update(ReviewQueries.DECREASE_USEFUL_QUERY.toString(), "Не удалось уменьшить рейтинг отзыва при отметке дизлайком", reviewId);
        log.trace("Рейтинг полезности отзыва уменьшен на 1");
    }

    @Override
    public Review create(Review review) {
        long id = insert(ReviewQueries.INSERT_REVIEW_QUERY.toString(), review.getUserId(), review.getFilmId(),
                review.getContent(), review.getIsPositive(), review.getUseful());
        review.setReviewId(id);
        return review;
    }

    @Override
    public Review update(Review newReview) {
        update(ReviewQueries.UPDATE_QUERY.toString(), "Не удалось обновить данные отзыва", newReview.getUserId(),
                newReview.getFilmId(), newReview.getContent(), newReview.getIsPositive(), newReview.getUseful(), newReview.getReviewId());
        return newReview;
    }

    @Override
    public boolean delete(Long reviewId) {
        return delete(ReviewQueries.DELETE_QUERY.toString(), reviewId);
    }
}
