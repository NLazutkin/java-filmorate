package ru.yandex.practicum.filmorate.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.review.NewReviewRequest;
import ru.yandex.practicum.filmorate.dto.review.ReviewDto;
import ru.yandex.practicum.filmorate.dto.review.UpdateReviewRequest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.ReviewMapper;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ReviewService {
    ReviewStorage reviewStorage;
    FilmStorage filmStorage;
    UserStorage userStorage;

    @Autowired
    public ReviewService(@Qualifier("ReviewDbStorage"/*"InMemoryReviewStorage"*/) ReviewStorage reviewStorage,
                         @Qualifier("FilmDbStorage"/*"InMemoryFilmStorage"*/) FilmStorage filmStorage,
                         @Qualifier("UserDbStorage"/*"InMemoryUserStorage"*/) UserStorage userStorage) {
        this.reviewStorage = reviewStorage;
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public ReviewDto findReview(Long reviewId) {
        log.debug(String.format("Поиск отзыва с ID %d", reviewId));

        return ReviewMapper.mapToReviewDto(reviewStorage.findReview(reviewId));
    }

    public Collection<ReviewDto> findAll() {
        log.debug("Получаем список всех отзывов");
        return reviewStorage.findAll().stream()
                .map(ReviewMapper::mapToReviewDto)
                .collect(Collectors.toList());
    }

    public Collection<ReviewDto> reviewsByFilmId(Long film_id, Integer count) {
        log.debug(String.format("Получаем список из %d отзывов", count));

        return reviewStorage.reviewsByFilmId(film_id, count).stream()
                .map(ReviewMapper::mapToReviewDto)
                .collect(Collectors.toList());
    }

    public void addLike(Long reviewId, Long userId, boolean isPositive) {
        log.trace("Попытка пользователя поставить лайк отзыву...");

        Review review = reviewStorage.findReview(reviewId);
        User user = userStorage.findUser(userId);

        reviewStorage.addLike(review, user, isPositive);

        log.debug(String.format("Пользователь %s ставит лайк фильму %d", user.getName(), review.getId()));
    }

    public void deleteLike(Long reviewId, Long userId, boolean isPositive) {
        log.trace("Попытка удалить лайк с отзыва...");

        Review review = reviewStorage.findReview(reviewId);
        User user = userStorage.findUser(userId);

        reviewStorage.deleteLike(review, user, isPositive);

        log.debug(String.format("Пользователь %s убирает лайк с отзыва %d", user.getName(), review.getId()));
    }

    public ReviewDto create(NewReviewRequest request) {
        log.debug(String.format("Создаем отзыв пользователя в id %d, для фильма c id %d", request.getUser_id(), request.getFilm_id()));
        Review review = ReviewMapper.mapToReview(request);

        Film film = filmStorage.findFilm(review.getFilm_id());
        User user = userStorage.findUser(review.getUser_id());

        Review createdReview = reviewStorage.create(review);

        log.trace(String.format("Отзыв пользователя %s о фильме %S сохранен под номером %d!",
                user.getName(), film.getName(), createdReview.getId()));
        return ReviewMapper.mapToReviewDto(createdReview);
    }

    public ReviewDto update(UpdateReviewRequest request) {
        log.debug("Обновляем данные отзыва");

        if (request.getId() == null) {
            throw new ValidationException("Не указан Id отзыва!");
        }

        Review updatedReview = ReviewMapper.updateReviewFields(reviewStorage.findReview(request.getId()), request);
        updatedReview = reviewStorage.update(updatedReview);

        return ReviewMapper.mapToReviewDto(updatedReview);
    }

    public boolean delete(Long reviewId) {
        Review review = reviewStorage.findReview(reviewId);
        log.debug("Удаляем данные отзыва №" + review.getId());
        return filmStorage.delete(reviewId);
    }
}
