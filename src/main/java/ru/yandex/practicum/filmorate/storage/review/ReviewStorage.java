package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;

public interface ReviewStorage {
    Review create(Review review);

    Review findReview(Long reviewId);

    Collection<Review> reviewsByFilmId(Long filmId, Integer count);

    Review update(Review newReview);

    boolean delete(Long reviewId);

    void increaseUseful(Long reviewId);

    void decreaseUseful(Long reviewId);
}