package ru.yandex.practicum.filmorate.storage.director;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.enums.query.DirectorQueries;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;
import ru.yandex.practicum.filmorate.storage.mappers.director.DirectorBaseRowMapper;

import java.util.Collection;

@Slf4j
@Component("DirectorDbStorage")
@FieldDefaults(makeFinal = true, level = AccessLevel.PROTECTED)
public class DirectorDbStorage extends BaseDbStorage<Director> implements DirectorStorage {
    RowMapper<Director> baseMapper;

    @Autowired
    public DirectorDbStorage(JdbcTemplate jdbc, DirectorBaseRowMapper baseMapper) {
        super(jdbc);
        this.baseMapper = baseMapper;
    }

    @Override
    public boolean isDirectorWithSameNameExist(String name) {
        return findOne(DirectorQueries.FIND_BY_NAME_QUERY.toString(), baseMapper, name).isPresent();
    }

    @Override
    public Director findDirector(Director director) {
        return findOne(DirectorQueries.FIND_BY_ID_QUERY.toString(), baseMapper, director.getId())
                .orElseThrow(() -> new GenreNotFoundException(String.format("В запросе не корректный режиссер с ID %d", director.getId())));
    }

    @Override
    public Director findDirector(Long directorId) {
        return findOne(DirectorQueries.FIND_BY_ID_QUERY.toString(), baseMapper, directorId)
                .orElseThrow(() -> new NotFoundException(String.format("Режиссер с ID %d не найден", directorId)));
    }

    @Override
    public Collection<Director> findAll() {
        return findMany(DirectorQueries.FIND_ALL_QUERY.toString(), baseMapper);
    }

    @Override
    public Director create(Director director) {
        long id = insert(DirectorQueries.INSERT_DIRECTOR_QUERY.toString(), director.getName());
        director.setId(id);
        return director;
    }

    @Override
    public Director update(Director newDirector) {
        update(DirectorQueries.UPDATE_QUERY.toString(), "Не удалось обновить данные о режиссере",
                newDirector.getName(), newDirector.getId());
        return newDirector;
    }

    @Override
    public boolean delete(Long directorId) {
        return delete(DirectorQueries.DELETE_QUERY.toString(), directorId);
    }
}
