package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.review.NewReviewRequest;
import ru.yandex.practicum.filmorate.dto.review.ReviewDto;
import ru.yandex.practicum.filmorate.dto.review.UpdateReviewRequest;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.Collection;

@RestController
@RequestMapping("/reviews")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ReviewController {
    ReviewService reviewService;
    String reviewLikePath = "/{id}/like/{user-id}";
    String reviewDislikePath = "/{id}/dislike/{user-id}";

    @PutMapping(reviewLikePath)
    public void addLike(@PathVariable("id") Long reviewId, @PathVariable("user-id") Long userId) {
        reviewService.addLike(reviewId, userId);
    }

    @DeleteMapping(reviewLikePath)
    public boolean deleteLike(@PathVariable("id") Long reviewId, @PathVariable("user-id") Long userId) {
        return reviewService.deleteLike(reviewId, userId);
    }

    @PutMapping(reviewDislikePath)
    public void addDislike(@PathVariable("id") Long reviewId, @PathVariable("user-id") Long userId) {
        reviewService.addDislike(reviewId, userId);
    }

    @DeleteMapping(reviewDislikePath)
    public boolean deleteDislike(@PathVariable("id") Long reviewId, @PathVariable("user-id") Long userId) {
        return reviewService.deleteDislike(reviewId, userId);
    }

    @GetMapping
    public Collection<ReviewDto> reviewsByFilmId(@RequestParam(name = "filmId", defaultValue = "0") Long filmId,
                                                 @RequestParam(name = "count", defaultValue = "10") Integer count) {
        return reviewService.reviewsByFilmId(filmId, count);
    }

    @GetMapping("/{id}")
    public ReviewDto findReview(@PathVariable("id") Long reviewId) {
        return reviewService.findReview(reviewId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewDto create(@Valid @RequestBody NewReviewRequest review) {
        return reviewService.create(review);
    }

    @PutMapping
    public ReviewDto update(@Valid @RequestBody UpdateReviewRequest newReview) {
        return reviewService.update(newReview);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id") Long reviewId) {
        return reviewService.delete(reviewId);
    }
}
