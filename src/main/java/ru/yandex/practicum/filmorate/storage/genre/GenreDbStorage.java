package ru.yandex.practicum.filmorate.storage.genre;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.enums.query.GenreQueries;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mappers.genre.GenreBaseRowMapper;

import java.util.Collection;

@Slf4j
@Component("GenreDbStorage")
@FieldDefaults(makeFinal = true, level = AccessLevel.PROTECTED)
public class GenreDbStorage extends BaseDbStorage<Genre> implements GenreStorage {
    RowMapper<Genre> baseMapper;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbc, GenreBaseRowMapper baseMapper) {
        super(jdbc);
        this.baseMapper = baseMapper;
    }

    @Override
    public boolean isGenreWithSameNameExist(String name) {
        return findOne(GenreQueries.FIND_BY_NAME_QUERY.toString(), baseMapper, name).isPresent();
    }

    @Override
    public Genre findGenre(Genre genre) {
        return findOne(GenreQueries.FIND_BY_ID_QUERY.toString(), baseMapper, genre.getId())
                .orElseThrow(() -> new GenreNotFoundException(String.format("В запросе не корректный жанр с ID %d", genre.getId())));
    }

    @Override
    public Genre findGenre(Long genreId) {
        return findOne(GenreQueries.FIND_BY_ID_QUERY.toString(), baseMapper, genreId)
                .orElseThrow(() -> new NotFoundException(String.format("Жанр с ID %d не найден", genreId)));
    }

    @Override
    public Collection<Genre> findAll() {
        return findMany(GenreQueries.FIND_ALL_QUERY.toString(), baseMapper);
    }

    @Override
    public Genre create(Genre genre) {
        long id = insert(GenreQueries.INSERT_GENRE_QUERY.toString(), genre.getName());
        genre.setId(id);
        return genre;
    }

    @Override
    public Genre update(Genre newGenre) {
        update(GenreQueries.UPDATE_QUERY.toString(), "Не удалось обновить данные о жанре",
                newGenre.getName(), newGenre.getId());
        return newGenre;
    }

    @Override
    public boolean delete(Long genreId) {
        return delete(GenreQueries.DELETE_QUERY.toString(), genreId);
    }
}
