package ru.yandex.practicum.filmorate.storage.estimation;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Estimation;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component("InMemoryEstimationStorage")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class InMemoryEstimationStorage implements EstimationStorage {
    Set<Estimation> estimations = new HashSet<>();


    @Override
    public Optional<Estimation> findEstimation(Long reviewId, Long userId) {
        if (!estimations.isEmpty()) {
            return estimations.stream()
                    .filter(estimation -> estimation.equals(new Estimation(reviewId, userId, Boolean.TRUE)))
                    .findFirst();
        }

        return Optional.empty();
    }

    @Override
    public void addEstimation(Review review, User user, Boolean isLike) {
        estimations.add(new Estimation(review.getReviewId(), user.getId(), isLike));
    }

    @Override
    public boolean deleteEstimation(Long reviewId, Long userId, Boolean isLike) {
        estimations.remove(new Estimation(reviewId, userId, isLike));
        return true;
    }
}
