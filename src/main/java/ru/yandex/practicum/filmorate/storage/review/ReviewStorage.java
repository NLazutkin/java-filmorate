package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.LinkedHashSet;

public interface ReviewStorage {
    Review findReview(Long reviewId);

    Review create(Review review);

    Review update(Review newReview);

    boolean delete(Long reviewId);

    Collection<Review> findAll();

    Collection<Review> reviewsByFilmId(Long film_id, Integer count);

    void addLike(Review review, User user, boolean isPositive);

    void deleteLike(Review review, User user, boolean isPositive);

    LinkedHashSet<Long> getLikes(Long reviewId);
}