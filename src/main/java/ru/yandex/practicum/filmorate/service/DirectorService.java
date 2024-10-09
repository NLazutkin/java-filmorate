package ru.yandex.practicum.filmorate.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.director.DirectorDto;
import ru.yandex.practicum.filmorate.dto.director.NewDirectorRequest;
import ru.yandex.practicum.filmorate.dto.director.UpdateDirectorRequest;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.DirectorMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;


import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DirectorService {
    DirectorStorage directorStorage;

    @Autowired
    public DirectorService(@Qualifier("DirectorDbStorage"/*"InMemoryDirectorStorage"*/) DirectorStorage genreStorage) {
        this.directorStorage = genreStorage;
    }

    public DirectorDto findDirector(Long genreId) {
        return DirectorMapper.mapToDirectorDto(directorStorage.findDirector(genreId));
    }

    public Collection<DirectorDto> findAll() {
        log.debug("Получаем записи о всех режиссерах");
        return directorStorage.findAll().stream().map(DirectorMapper::mapToDirectorDto).collect(Collectors.toList());
    }

    public DirectorDto create(NewDirectorRequest request) {
        log.debug("Создаем запись о режиссере");

        if (directorStorage.isDirectorWithSameNameExist(request.getName())) {
            throw new DuplicatedDataException(String.format("Режиссер с именем \"%s\" уже существует", request.getName()));
        }

        Director director = DirectorMapper.mapToDirector(request);
        director = directorStorage.create(director);

        return DirectorMapper.mapToDirectorDto(director);
    }

    public DirectorDto update(UpdateDirectorRequest request) {
        log.debug("Обновляем данные о режиссерах");

        if (request.getId() == null) {
            throw new ValidationException("Id режиссера должен быть указан");
        }

        Director updatedGenre = DirectorMapper.updateDirectorFields(directorStorage.findDirector(request.getId()), request);
        updatedGenre = directorStorage.update(updatedGenre);

        return DirectorMapper.mapToDirectorDto(updatedGenre);
    }

    public boolean delete(Long directorId) {
        Director director = directorStorage.findDirector(directorId);
        log.debug(String.format("Удаляем данные режиссера %s", director.getName()));
        return directorStorage.delete(directorId);
    }
}
