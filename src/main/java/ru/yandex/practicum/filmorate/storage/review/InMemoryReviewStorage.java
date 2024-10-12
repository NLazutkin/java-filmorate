package ru.yandex.practicum.filmorate.storage.review;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component("InMemoryReviewStorage")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class InMemoryReviewStorage implements ReviewStorage {
    Map<Long, Review> reviews = new HashMap<>();

    // вспомогательный метод для генерации идентификатора нового поста
    private long getNextId() {
        long currentMaxId = reviews.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @Override
    public Review findReview(Long reviewId) {
        return Optional.ofNullable(reviews.get(reviewId))
                .orElseThrow(() -> new NotFoundException(String.format("Отзыв №%d не найден", reviewId)));
    }

    @Override
    public Collection<Review> reviewsByFilmId(Long filmId, Integer count) {
        log.debug("Получаем список из {} отзывов фильма с ID {}", count, filmId);

        if (filmId == 0L) {
            return reviews.values()
                    .stream()
                    .limit(count)
                    .collect(Collectors.toList());
        }

        return reviews.values()
                .stream()
                .filter(review -> Objects.equals(review.getFilmId(), filmId))
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public void increaseUseful(Long reviewId) {
        findReview(reviewId).increaseUseful();
        log.trace("Рейтинг полезности отзыва увеличен на 1");
    }

    @Override
    public void decreaseUseful(Long reviewId) {
        findReview(reviewId).decreaseUseful();
        log.trace("Рейтинг полезности отзыва уменьшен на 1");
    }

    @Override
    public Review create(Review review) {
        review.setReviewId(getNextId());
        reviews.put(review.getReviewId(), review);

        return review;
    }

    @Override
    public Review update(Review newReview) {
        reviews.put(newReview.getReviewId(), newReview);
        log.trace("Данные об отзыве {} обновлены!", newReview.getReviewId());

        return newReview;
    }

    @Override
    public boolean delete(Long reviewId) {
        reviews.remove(reviewId);

        return Optional.ofNullable(reviews.get(reviewId)).isPresent();
    }
}
