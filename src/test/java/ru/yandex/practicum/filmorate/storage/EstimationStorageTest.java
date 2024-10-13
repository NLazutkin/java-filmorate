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
import ru.yandex.practicum.filmorate.storage.estimation.EstimationDbStorage;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@ComponentScan("ru.yandex.practicum.filmorate")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class EstimationStorageTest {
    EstimationDbStorage estimationStorage;

    @Test
    public void testFindEstimation() {
        assertThat(estimationStorage.findEstimation(2L, 1L))
                .isPresent()
                .get()
                .isInstanceOf(Estimation.class)
                .hasFieldOrPropertyWithValue("reviewId", 2L)
                .hasFieldOrPropertyWithValue("userId", 1L)
                .hasFieldOrPropertyWithValue("isLike", Boolean.TRUE);
    }

    @Test
    public void testAddEstimation() {
        Review newReview = new Review();
        newReview.setReviewId(3L);
        newReview.setUserId(2L);
        newReview.setFilmId(1L);
        newReview.setContent("Отзыв");
        newReview.setIsPositive(Boolean.FALSE);
        newReview.setUseful(15);

        User newUser = new User();
        newUser.setId(3L);
        newUser.setEmail("Jack@yandex.ru");
        newUser.setName("Jack");
        newUser.setLogin("Jack");
        newUser.setBirthday(LocalDate.of(2002, 2, 2));

        estimationStorage.addEstimation(newReview, newUser, Boolean.TRUE);

        assertThat(estimationStorage.findEstimation(3L, 3L))
                .isPresent()
                .get()
                .isInstanceOf(Estimation.class)
                .hasFieldOrPropertyWithValue("reviewId", 3L)
                .hasFieldOrPropertyWithValue("userId", 3L)
                .hasFieldOrPropertyWithValue("isLike", Boolean.TRUE);
    }

    @Test
    public void testDeleteEstimation() {
        assertThat(estimationStorage.deleteEstimation(1L, 2L, Boolean.TRUE)).isTrue();
    }
}