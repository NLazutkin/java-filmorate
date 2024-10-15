package ru.yandex.practicum.filmorate.storage.estimation;

import ru.yandex.practicum.filmorate.model.Estimation;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Optional;

public interface EstimationStorage {
    Optional<Estimation> findEstimation(Long reviewId, Long userId);

    void addEstimation(Review review, User user, Boolean isLike);

    boolean deleteEstimation(Long reviewId, Long userId, Boolean isLike);
}