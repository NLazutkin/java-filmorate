package ru.yandex.practicum.filmorate.storage;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.review.ReviewDbStorage;

import static org.assertj.core.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@ComponentScan("ru.yandex.practicum.filmorate")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ReviewStorageTest {
    ReviewDbStorage reviewStorage;

    @Test
    public void testFindReview() {
        assertThat(reviewStorage.findReview(1L))
                .isInstanceOf(Review.class)
                .hasFieldOrPropertyWithValue("reviewId", 1L)
                .hasFieldOrPropertyWithValue("content", "Хороший отзыв");
    }

    @Test
    public void testReviewsByFilmId() {
        assertThat(reviewStorage.reviewsByFilmId(4L, 10)).isNotEmpty()
                .hasSize(2)
                .filteredOn("isPositive", Boolean.FALSE)
                .isNotEmpty()
                .hasExactlyElementsOfTypes(Review.class);
    }

    @Test
    public void testIncreaseUseful() {
        reviewStorage.increaseUseful(1L);

        assertThat(reviewStorage.findReview(1L))
                .isInstanceOf(Review.class)
                .hasFieldOrPropertyWithValue("useful", 3)
                .hasFieldOrPropertyWithValue("content", "Хороший отзыв");
    }

    @Test
    public void testDecreaseUseful() {
        reviewStorage.decreaseUseful(1L);

        assertThat(reviewStorage.findReview(1L))
                .isInstanceOf(Review.class)
                .hasFieldOrPropertyWithValue("useful", 1)
                .hasFieldOrPropertyWithValue("content", "Хороший отзыв");
    }

    @Test
    public void testCreate() {
        Review newReview = new Review();
        newReview.setUserId(1L);
        newReview.setFilmId(1L);
        newReview.setContent("Хороший отзыв");
        newReview.setIsPositive(Boolean.TRUE);
        newReview.setUseful(15);

        assertThat(reviewStorage.create(newReview))
                .isNotNull()
                .hasFieldOrPropertyWithValue("reviewId", 6L);
    }

    @Test
    public void testUpdate() {
        Review newReview = new Review();
        newReview.setReviewId(3L);
        newReview.setUserId(3L);
        newReview.setFilmId(2L);
        newReview.setContent("Просто отзыв");
        newReview.setIsPositive(Boolean.TRUE);
        newReview.setUseful(777);

        assertThat(reviewStorage.update(newReview))
                .isNotNull()
                .hasFieldOrPropertyWithValue("reviewId", 3L)
                .hasFieldOrPropertyWithValue("content", "Просто отзыв")
                .hasFieldOrPropertyWithValue("useful", 777);
    }

    @Test
    public void testDelete() {
        assertThat(reviewStorage.delete(5L)).isTrue();
    }
}