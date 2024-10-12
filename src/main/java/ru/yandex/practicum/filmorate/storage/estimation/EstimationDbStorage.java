package ru.yandex.practicum.filmorate.storage.estimation;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.enums.query.EstimationQueries;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;
import ru.yandex.practicum.filmorate.storage.mappers.estimation.EstimationBaseRowMapper;

import java.util.Optional;

@Slf4j
@Component("EstimationDbStorage")
@FieldDefaults(makeFinal = true, level = AccessLevel.PROTECTED)
public class EstimationDbStorage extends BaseDbStorage<Estimation> implements EstimationStorage {
    RowMapper<Estimation> baseMapper;

    @Autowired
    public EstimationDbStorage(JdbcTemplate jdbc, EstimationBaseRowMapper baseMapper) {
        super(jdbc);
        this.baseMapper = baseMapper;
    }

    @Override
    public Optional<Estimation> findEstimation(Long reviewId, Long userId) {
        return findOne(EstimationQueries.FIND_BY_IDS_QUERY.toString(), baseMapper, reviewId, userId);
    }

    @Override
    public void addEstimation(Review review, User user, Boolean isLike) {
        String errMsg = String.format("Пользователь %s не смог поставить оценку %b фильму %s",
                user.getName(), isLike, review.getReviewId());

        update(EstimationQueries.INSERT_ESTIMATION_QUERY.toString(), errMsg, review.getReviewId(), user.getId(), isLike);
    }

    @Override
    public boolean deleteEstimation(Long reviewId, Long userId, Boolean isLike) {
        return delete(EstimationQueries.DELETE_ESTIMATION_QUERY.toString(), reviewId, userId, isLike);
    }
}