package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class ReviewController {
    private final ReviewService reviewService;
    private final String reviewLikePath = "/{id}/like/{user-id}";
    private final String reviewDislikePath = "/{id}/dislike/{user-id}";

    @PutMapping(reviewLikePath)
    public void addLike(@PathVariable("id") Long filmId, @PathVariable("user-id") Long userId) {
        reviewService.addLike(filmId, userId);
    }

    @DeleteMapping(reviewLikePath)
    public void deleteLike(@PathVariable("id") Long filmId, @PathVariable("user-id") Long userId) {
        reviewService.deleteLike(filmId, userId);
    }

    @PutMapping(reviewDislikePath)
    public void addDislike(@PathVariable("id") Long filmId, @PathVariable("user-id") Long userId) {
        reviewService.addDislike(filmId, userId);
    }

    @DeleteMapping(reviewDislikePath)
    public void deleteDislike(@PathVariable("id") Long filmId, @PathVariable("user-id") Long userId) {
        reviewService.deleteDislike(filmId, userId);
    }

    @GetMapping("/popular")
    public Collection<ReviewDto> findPopular(@RequestParam(name = "count", defaultValue = "10") Integer count) {
        return reviewService.findPopular(count);
    }

    @GetMapping("/{id}")
    public ReviewDto findReview(@PathVariable("id") Long reviewId) {
        return reviewService.findReview(reviewId);
    }

    @GetMapping
    public Collection<ReviewDto> findAll() {
        return reviewService.findAll();
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
