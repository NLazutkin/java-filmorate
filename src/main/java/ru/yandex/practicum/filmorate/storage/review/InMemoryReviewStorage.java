package ru.yandex.practicum.filmorate.storage.review;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component("InMemoryFilmStorage")
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
    public Collection<Review> findAll() {
        return reviews.values();
    }

    @Override
    public Review findReview(Long reviewId) {
        return Optional.ofNullable(reviews.get(reviewId))
                .orElseThrow(() -> new NotFoundException(String.format("Отзыв №%d не найден", reviewId)));
    }

    @Override
    public Collection<Review> reviewsByFilmId(Long film_id, Integer count) {
        return findAll()
                .stream()
                .sorted(Comparator.comparing((Review review) -> review.getLikes().size(), Comparator.reverseOrder()))
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public void addLike(Review review, User user, boolean isPositive) {
        if (review.getLikes().contains(user.getId())) {
            throw new DuplicatedDataException(String.format("Пользователь %s уже ставил лайк фильму №%d",
                    user.getName(), review.getId()));
        }

        review.getLikes().add(user.getId());
    }

    public LinkedHashSet<Long> getLikes(Long reviewId) {
        Review review = reviews.get(reviewId);
        LinkedHashSet<Long> likes = review.getLikes();

        if (likes == null || likes.isEmpty()) {
            log.trace(String.format("У отзыва %d нет лайков", review.getId()));
            return new LinkedHashSet<>();
        }

        return likes;
    }

    @Override
    public void deleteLike(Review review, User user, boolean isPositive) {
        if (!review.getLikes().contains(user.getId())) {
            throw new NotFoundException(String.format("Пользователь %s не ставил лайк отзыву №%d. Удалить лайк невозможно",
                    user.getName(), review.getId()));
        }

        review.getLikes().remove(user.getId());
    }

    @Override
    public Review create(Review review) {
        review.setId(getNextId());
        reviews.put(review.getId(), review);

        return review;
    }

    @Override
    public Review update(Review newReview) {
        reviews.put(newReview.getId(), newReview);
        log.trace(String.format("Данные об отзыве %d обновлены!",  newReview.getId()));

        return newReview;
    }

    @Override
    public boolean delete(Long reviewId) {
        reviews.remove(reviewId);

        return Optional.ofNullable(reviews.get(reviewId)).isPresent();
    }
}
