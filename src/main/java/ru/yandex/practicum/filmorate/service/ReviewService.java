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
import ru.yandex.practicum.filmorate.enums.actions.EventType;
import ru.yandex.practicum.filmorate.enums.actions.OperationType;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.ReviewMapper;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.estimation.EstimationStorage;
import ru.yandex.practicum.filmorate.storage.feed.FeedStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ReviewService {
    ReviewStorage reviewStorage;
    FilmStorage filmStorage;
    UserStorage userStorage;
    EstimationStorage estimationStorage;
    FeedStorage feedStorage;

    @Autowired
    public ReviewService(@Qualifier("ReviewDbStorage"/*"InMemoryReviewStorage"*/) ReviewStorage reviewStorage,
                         @Qualifier("FilmDbStorage"/*"InMemoryFilmStorage"*/) FilmStorage filmStorage,
                         @Qualifier("UserDbStorage"/*"InMemoryUserStorage"*/) UserStorage userStorage,
                         @Qualifier("EstimationDbStorage"/*"InMemoryEstimationStorage"*/) EstimationStorage estimationStorage,
                         @Qualifier("FeedDbStorage"/*InMemoryFeedStorage*/) FeedStorage feedStorage) {
        this.reviewStorage = reviewStorage;
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.estimationStorage = estimationStorage;
        this.feedStorage = feedStorage;
    }

    public ReviewDto findReview(Long reviewId) {
        log.debug("Поиск отзыва с ID {}", reviewId);

        return ReviewMapper.mapToReviewDto(reviewStorage.findReview(reviewId));
    }

    public Collection<ReviewDto> reviewsByFilmId(Long filmId, Integer count) {
        log.debug("Получаем список отзывов");

        return reviewStorage.reviewsByFilmId(filmId, count).stream()
                .map(ReviewMapper::mapToReviewDto)
                .sorted(Comparator.comparingInt(ReviewDto::getUseful).reversed())
                .collect(Collectors.toList());
    }

    public void addLike(Long reviewId, Long userId) {
        log.trace("Попытка пользователя поставить лайк отзыву...");

        Review review = reviewStorage.findReview(reviewId);
        User user = userStorage.findUser(userId);

        Optional<Estimation> estimation = estimationStorage.findEstimation(reviewId, userId);
        if (estimation.isPresent()) {
            if (estimation.get().getIsLike().equals(Boolean.TRUE)) {
                log.trace("Лайк отзыву c №{} уже существует", reviewId);
                return;
            } else {
                log.trace("На отзыве c №{} уже существует дизлайк. Удаляем дизлайк и ставил лайк", reviewId);
                estimationStorage.deleteEstimation(reviewId, userId, Boolean.FALSE);
                reviewStorage.increaseUseful(review.getReviewId());
            }
        }

        estimationStorage.addEstimation(review, user, Boolean.TRUE);

        log.trace("Пользователь {} ставит лайк отзыву №{}", user.getName(), review.getReviewId());
        reviewStorage.increaseUseful(review.getReviewId());
    }

    public void addDislike(Long reviewId, Long userId) {
        log.trace("Попытка пользователя поставить дизлайк отзыву...");

        Review review = reviewStorage.findReview(reviewId);
        User user = userStorage.findUser(userId);

        Optional<Estimation> estimation = estimationStorage.findEstimation(reviewId, userId);
        if (estimation.isPresent()) {
            if (estimation.get().getIsLike().equals(Boolean.FALSE)) {
                log.trace("Дизлайк отзыву c №{} уже существует", reviewId);
                return;
            } else {
                log.trace("На отзыве c №{} уже существует лайк. Удаляем лайк и ставим дизлайк", reviewId);
                estimationStorage.deleteEstimation(reviewId, userId, Boolean.TRUE);
                reviewStorage.decreaseUseful(review.getReviewId());
            }
        }

        estimationStorage.addEstimation(review, user, Boolean.FALSE);

        log.trace("Пользователь {} ставит дизлайк отзыву №{}", user.getName(), review.getReviewId());
        reviewStorage.decreaseUseful(review.getReviewId());
    }

    public boolean deleteLike(Long reviewId, Long userId) {
        log.trace("Попытка удалить лайк с отзыва...");

        boolean result = estimationStorage.deleteEstimation(reviewId, userId, Boolean.TRUE);

        if (result) {
            log.trace("Пользователь удаляет лайк отзыву");
            reviewStorage.decreaseUseful(reviewId);
        }

        return result;
    }

    public boolean deleteDislike(Long reviewId, Long userId) {
        log.trace("Попытка удалить дизлайк с отзыва...");

        boolean result = estimationStorage.deleteEstimation(reviewId, userId, Boolean.FALSE);

        if (result) {
            log.trace("Пользователь удаляет дизлайк отзыву");
            reviewStorage.decreaseUseful(reviewId);
        }

        return result;
    }

    public ReviewDto create(NewReviewRequest request) {
        log.debug("Создаем отзыв пользователя под номером {}, для фильма c id {}", request.getUserId(), request.getFilmId());
        Review review = ReviewMapper.mapToReview(request);

        Film film = filmStorage.findFilm(review.getFilmId());
        User user = userStorage.findUser(review.getUserId());

        Review createdReview = reviewStorage.create(review);

        Feed feed = new Feed();
        feed.setUserId(review.getUserId());
        feed.setTimestamp(System.currentTimeMillis());
        feed.setEventType(EventType.REVIEW);
        feed.setOperation(OperationType.ADD);
        feed.setEntityId(createdReview.getReviewId());
        feedStorage.addEvent(feed);

        log.trace("Отзыв пользователя {} о фильме {} сохранен под номером {}",
                user.getName(), film.getName(), createdReview.getReviewId());
        return ReviewMapper.mapToReviewDto(createdReview);
    }

    public ReviewDto update(UpdateReviewRequest request) {
        log.debug("Обновляем данные отзыва");

        if (request.getReviewId() == null) {
            throw new ValidationException("Не указан № отзыва!");
        }

        Review updatedReview = ReviewMapper.updateReviewFields(reviewStorage.findReview(request.getReviewId()), request);
        updatedReview = reviewStorage.update(updatedReview);

        Feed feed = new Feed();
        feed.setUserId(updatedReview.getUserId());
        feed.setTimestamp(System.currentTimeMillis());
        feed.setEventType(EventType.REVIEW);
        feed.setOperation(OperationType.UPDATE);
        feed.setEntityId(updatedReview.getReviewId());
        feedStorage.addEvent(feed);


        return ReviewMapper.mapToReviewDto(updatedReview);
    }

    public boolean delete(Long reviewId) {
        Review review = reviewStorage.findReview(reviewId);
        log.debug("Удаляем данные отзыва №{}", review.getReviewId());
        boolean result = reviewStorage.delete(reviewId);

        if (result) {
            Feed feed = new Feed();
            feed.setUserId(review.getUserId());
            feed.setTimestamp(System.currentTimeMillis());
            feed.setEventType(EventType.REVIEW);
            feed.setOperation(OperationType.REMOVE);
            feed.setEntityId(review.getReviewId());
            feedStorage.addEvent(feed);
        }

        return result;
    }
}
