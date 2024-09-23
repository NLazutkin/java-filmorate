package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;

import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@ComponentScan("ru.yandex.practicum.filmorate")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaStorageTest {
    private final MpaDbStorage mpaStorage;

    @Test
    public void testFindGenre() {
        assertThat(mpaStorage.findMpa(1L))
                .isInstanceOf(Mpa.class)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "G");;
    }

    @Test
    public void findAll() {
        assertThat(mpaStorage.findAll()).isNotEmpty()
                .hasSize(5)
                .filteredOn("name", "PG-13")
                .isNotEmpty()
                .hasExactlyElementsOfTypes(Mpa.class);
    }

    @Test
    public void create() {
        Mpa newMpa = new Mpa();
        newMpa.setName("PG-18");
        newMpa.setDescription("Лицам после 18 лет просмотр разрешен");

        assertThat(mpaStorage.create(newMpa))
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 6L)
                .hasFieldOrPropertyWithValue("name", "PG-18");
    }

    @Test
    public void update() {
        Mpa newMpa = new Mpa();
        newMpa.setId(1L);
        newMpa.setName("GG");
        newMpa.setDescription("У фильма нет возрастных ограничений");

        assertThat(mpaStorage.update(newMpa))
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "GG");
    }

    @Test
    public void delete() {
        assertThat(mpaStorage.delete(6L)).isFalse();
    }

}
