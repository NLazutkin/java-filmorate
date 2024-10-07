package ru.yandex.practicum.filmorate.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.dto.genre.NewGenreRequest;
import ru.yandex.practicum.filmorate.dto.genre.UpdateGenreRequest;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class GenreService {
    GenreStorage genreStorage;

    @Autowired
    public GenreService(@Qualifier("GenreDbStorage"/*"InMemoryGenreStorage"*/) GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public GenreDto findGenre(Long genreId) {
        return GenreMapper.mapToGenreDto(genreStorage.findGenre(genreId));
    }

    public Collection<GenreDto> findAll() {
        log.debug("Получаем записи о всех жанрах");
        return genreStorage.findAll().stream().map(GenreMapper::mapToGenreDto).collect(Collectors.toList());
    }

    public GenreDto create(NewGenreRequest request) {
        log.debug("Создаем запись о жанре");

        if (genreStorage.isGenreWithSameNameExist(request.getName())) {
            throw new DuplicatedDataException(String.format("Жанр с именем \"%s\" уже существует", request.getName()));
        }

        Genre genre = GenreMapper.mapToGenre(request);
        genre = genreStorage.create(genre);

        return GenreMapper.mapToGenreDto(genre);
    }

    public GenreDto update(UpdateGenreRequest request) {
        log.debug("Обновляем данные о жанрах");

        if (request.getId() == null) {
            throw new ValidationException("Id жанра должен быть указан");
        }

        Genre updatedGenre = GenreMapper.updateGenreFields(genreStorage.findGenre(request.getId()), request);
        updatedGenre = genreStorage.update(updatedGenre);

        return GenreMapper.mapToGenreDto(updatedGenre);
    }

    public boolean delete(Long genreId) {
        Genre genre = genreStorage.findGenre(genreId);
        log.debug(String.format("Удаляем данные жанра %s", genre.getName()));
        return genreStorage.delete(genreId);
    }
}
