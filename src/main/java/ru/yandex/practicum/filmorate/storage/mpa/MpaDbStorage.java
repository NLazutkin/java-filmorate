package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.enums.query.MpaQueries;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;
import ru.yandex.practicum.filmorate.storage.mappers.mpa.MpaBaseRowMapper;

import java.util.Collection;

@Slf4j
@Component("MpaDbStorage")
public class MpaDbStorage extends BaseDbStorage<Mpa> implements MpaStorage {
    protected final RowMapper<Mpa> baseMapper;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbc, MpaBaseRowMapper baseMapper) {
        super(jdbc);
        this.baseMapper = baseMapper;
    }

    @Override
    public boolean isMpaWithSameNameExist(String name) {
        return findOne(MpaQueries.FIND_BY_NAME_QUERY.toString(), baseMapper, name).isPresent();
    }

    @Override
    public Mpa findMpa(Mpa mpa) {
        return findOne(MpaQueries.FIND_BY_ID_QUERY.toString(), baseMapper, mpa.getId())
                .orElseThrow(() -> new MpaNotFoundException("В запросе не корректный рейтинг MPA с ID " + mpa.getId()));
    }

    @Override
    public Mpa findMpa(Long mpaId) {
        return findOne(MpaQueries.FIND_BY_ID_QUERY.toString(), baseMapper, mpaId)
                .orElseThrow(() -> new NotFoundException("Рейтинг MPA с ID " + mpaId + " не найден"));
    }

    @Override
    public Collection<Mpa> findAll() {
        return findMany(MpaQueries.FIND_ALL_QUERY.toString(), baseMapper);
    }

    @Override
    public Mpa create(Mpa mpa) {
        long id = insert(MpaQueries.INSERT_MPA_QUERY.toString(), mpa.getName(), mpa.getDescription());
        mpa.setId(id);
        return mpa;
    }

    @Override
    public Mpa update(Mpa newMpa) {
        update(MpaQueries.UPDATE_QUERY.toString(), "Не удалось обновить данные о рейтинге MPA",
                newMpa.getName(), newMpa.getDescription(), newMpa.getId());
        return newMpa;
    }

    @Override
    public boolean delete(Long id) {
        return delete(MpaQueries.DELETE_QUERY.toString(), id);
    }
}
