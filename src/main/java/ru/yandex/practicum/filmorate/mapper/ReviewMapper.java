package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.review.NewReviewRequest;
import ru.yandex.practicum.filmorate.dto.review.ReviewDto;
import ru.yandex.practicum.filmorate.dto.review.UpdateReviewRequest;
import ru.yandex.practicum.filmorate.model.Review;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReviewMapper {

    public static ReviewDto mapToReviewDto(Review review) {
        ReviewDto dto = new ReviewDto();
        dto.setId(review.getId());
        dto.setUser_id(review.getUser_id());
        dto.setFilm_id(review.getFilm_id());
        dto.setContent(review.getContent());
        dto.setPositive(review.isPositive());
        dto.setUseful(review.getUseful());

        return dto;
    }

    public static Review mapToReview(NewReviewRequest request) {
        Review film = new Review();
        film.setUser_id(request.getUser_id());
        film.setFilm_id(request.getFilm_id());
        film.setContent(request.getContent());
        film.setPositive(request.isPositive());
        film.setUseful(request.getUseful());

        return film;
    }

    public static Review updateReviewFields(Review review, UpdateReviewRequest request) {
        if (request.hasContent()) {
            review.setContent(request.getContent());
        }

        review.setPositive(request.isPositive());

        if (request.hasUseful()) {
            review.setUseful(request.getUseful());
        }

        return review;
    }
}

