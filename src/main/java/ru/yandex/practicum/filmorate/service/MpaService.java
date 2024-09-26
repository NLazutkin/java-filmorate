package ru.yandex.practicum.filmorate.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.dto.mpa.MpaDto;
import ru.yandex.practicum.filmorate.dto.mpa.NewMpaRequest;
import ru.yandex.practicum.filmorate.dto.mpa.UpdateMpaRequest;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MpaService {
    MpaStorage mpaStorage;

    @Autowired
    public MpaService(@Qualifier(/*"InMemoryMpaStorage"*/"MpaDbStorage") MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public MpaDto findMpa(Long userId) {
        return MpaMapper.mapToMpaDto(mpaStorage.findMpa(userId));
    }

    public Collection<MpaDto> findAll() {
        log.debug("Получаем записи о всех рейтингах MPA");
        return mpaStorage.findAll().stream().map(MpaMapper::mapToMpaDto).collect(Collectors.toList());
    }

    public MpaDto create(NewMpaRequest request) {
        log.debug("Создаем запись о рейтинге MPA");

        if (mpaStorage.isMpaWithSameNameExist(request.getName())) {
            throw new DuplicatedDataException(String.format("Рейтинг с именем \"%s\" уже существует", request.getName()));
        }

        Mpa mpa = MpaMapper.mapToMpa(request);
        mpa = mpaStorage.create(mpa);

        return MpaMapper.mapToMpaDto(mpa);
    }

    public MpaDto update(UpdateMpaRequest request) {
        log.debug("Обновляем данные рейтинга MPA");

        if (request.getId() == null) {
            throw new ValidationException("Id рейтинга MPA должен быть указан");
        }

        Mpa updatedMpa = MpaMapper.updateMpaFields(mpaStorage.findMpa(request.getId()), request);
        updatedMpa = mpaStorage.update(updatedMpa);

        return MpaMapper.mapToMpaDto(updatedMpa);
    }

    public boolean delete(Long mpaId) {
        Mpa mpa = mpaStorage.findMpa(mpaId);
        log.debug(String.format("Удаляем данные рейтинга %s", mpa.getName()));
        return mpaStorage.delete(mpaId);
    }
}
