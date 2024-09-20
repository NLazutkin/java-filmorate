package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component("InMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private final Map<Long, LinkedHashSet<Genre>> filmsGenres = new HashMap<>(); //???
    // ratings

    // вспомогательный метод для генерации идентификатора нового поста
    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film findFilm(Long filmId) {
        return Optional.ofNullable(films.get(filmId)).orElseThrow(() -> new NotFoundException("Фильм с ID " + filmId + " не найден"));
    }

    @Override
    public Collection<Film> findPopular(Integer count) {
        return findAll()
                .stream()
                .sorted(Comparator.comparing((Film film) -> film.getLikes().size(), Comparator.reverseOrder()))
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public void addLike(Film film, User user) {
        if (film.getLikes().contains(user.getId())) {
            throw new DuplicatedDataException("Пользователь " + user.getName()
                    + " уже ставил лайк фильму " + film.getName());
        }

        film.getLikes().add(user.getId());
    }

    @Override
    public void deleteLike(Film film, User user) {
        if (!film.getLikes().contains(user.getId())) {
            throw new NotFoundException("Пользователь " + user.getName()
                    + "не ставил лайк фильму " + film.getName() + ". Удалить лайк невозможно");
        }

        film.getLikes().remove(user.getId());
    }

    @Override
    public void addGenre(Genre genre, Film film) {
        Optional<LinkedHashSet<Genre>> filmGenre = Optional.ofNullable(filmsGenres.get(film.getId()));
        LinkedHashSet<Genre> genres = new LinkedHashSet<>();
        if (filmGenre.isPresent()) {
            genres = filmGenre.get();
        }
        genres.add(genre);
        filmsGenres.put(film.getId(), genres);
    }

    @Override
    public Set<Genre> findGenresById(Long filmId) {
        LinkedHashSet<Genre> genres = filmsGenres.get(filmId);
        if (genres.isEmpty()) {
            return new LinkedHashSet<>();
        }
        return genres;
    }

    @Override //???
    public Mpa findRatingById(Long filmId) {
        return new Mpa();
    }

    @Override
    public Film create(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        films.put(newFilm.getId(), newFilm);
        log.trace("Данные о фильме " + newFilm.getName() + " обновлены!");
        return newFilm;
    }

    @Override
    public boolean delete(Long filmId) {
        films.remove(filmId);
        return Optional.ofNullable(films.get(filmId)).isPresent();
    }
}
