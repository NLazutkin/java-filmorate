package ru.yandex.practicum.filmorate.storage.mappers.estimation;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Estimation;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class EstimationBaseRowMapper implements RowMapper<Estimation> {

    @Override
    public Estimation mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Estimation estimation = new Estimation();
        estimation.setReviewId(resultSet.getLong("review_id"));
        estimation.setUserId(resultSet.getLong("user_id"));
        estimation.setIsLike(resultSet.getBoolean("isLike"));
        return estimation;
    }
}
