package ru.yandex.practicum.filmorate.storage.mappers.film;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;


@Component
public class FilmBaseRowMapper implements RowMapper<Film> {

    private Mpa convertResultToMpa(Long mpaId) {
        Mpa mpa = new Mpa();
        mpa.setId(mpaId);
        return mpa;
    }

    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getLong("id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        film.setReleaseDate(resultSet.getDate("releaseDate").toLocalDate());
        film.setDuration(resultSet.getLong("duration"));
        film.setMpa(convertResultToMpa(resultSet.getLong("rating_id")));
        return film;
    }
}