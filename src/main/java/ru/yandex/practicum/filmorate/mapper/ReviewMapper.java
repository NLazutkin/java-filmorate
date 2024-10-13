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
        dto.setReviewId(review.getReviewId());
        dto.setUserId(review.getUserId());
        dto.setFilmId(review.getFilmId());
        dto.setContent(review.getContent());
        dto.setIsPositive(review.getIsPositive());
        dto.setUseful(review.getUseful());

        return dto;
    }

    public static Review mapToReview(NewReviewRequest request) {
        Review film = new Review();
        film.setUserId(request.getUserId());
        film.setFilmId(request.getFilmId());
        film.setContent(request.getContent());
        film.setIsPositive(request.getIsPositive());
        film.setUseful(request.getUseful());

        return film;
    }

    public static Review updateReviewFields(Review review, UpdateReviewRequest request) {
        if (request.hasContent()) {
            review.setContent(request.getContent());
        }

        review.setIsPositive(request.getIsPositive());

        return review;
    }
}

