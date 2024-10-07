package ru.yandex.practicum.filmorate.storage;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@ComponentScan("ru.yandex.practicum.filmorate")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreStorageTest {
    GenreDbStorage genreStorage;

    @Test
    public void testFindGenre() {
        assertThat(genreStorage.findGenre(1L))
                .isInstanceOf(Genre.class)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "Комедия");
    }

    @Test
    public void testFindAll() {
        assertThat(genreStorage.findAll()).isNotEmpty()
                .hasSize(6)
                .filteredOn("name", "Драма")
                .isNotEmpty()
                .hasExactlyElementsOfTypes(Genre.class);
    }

    @Test
    public void testCreate() {
        Genre newGenre = new Genre();
        newGenre.setName("Фантастика");

        assertThat(genreStorage.create(newGenre))
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 7L)
                .hasFieldOrPropertyWithValue("name", "Фантастика");
    }

    @Test
    public void testUpdate() {
        Genre newGenre = new Genre();
        newGenre.setId(1L);
        newGenre.setName("Супер комедия");

        assertThat(genreStorage.update(newGenre))
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "Супер комедия");
    }

    @Test
    public void testDelete() {
        assertThat(genreStorage.delete(6L)).isFalse();
    }

}