package ru.yandex.practicum.filmorate.storage.mappers.review;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ReviewBaseRowMapper implements RowMapper<Review> {

    @Override
    public Review mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Review review = new Review();
        review.setId(resultSet.getLong("id"));
        review.setUser_id(resultSet.getLong("user_id"));
        review.setFilm_id(resultSet.getLong("film_id"));
        review.setContent(resultSet.getString("content"));
        review.setPositive(resultSet.getBoolean("isPositive"));
        review.setUseful(resultSet.getInt("useful"));
        return review;
    }
}