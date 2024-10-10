package ru.yandex.practicum.filmorate.storage.mappers.director;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class DirectorBaseRowMapper implements RowMapper<Director> {
    @Override
    public Director mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Director genre = new Director();
        genre.setId(resultSet.getLong("id"));
        genre.setName(resultSet.getString("name"));

        return genre;
    }
}
