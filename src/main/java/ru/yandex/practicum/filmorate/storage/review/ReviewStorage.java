package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.LinkedHashSet;

public interface ReviewStorage {
    Film findFilm(Long reviewId);

    Film create(Review review);

    Review update(Review newReview);

    boolean delete(Long reviewId);

    Collection<Review> findAll();

    Collection<Review> findPopular(Integer count);

    void addLike(Review review, User user);

    void deleteLike(Review review, User user);

    LinkedHashSet<Long> getLikes(Long reviewId);
}