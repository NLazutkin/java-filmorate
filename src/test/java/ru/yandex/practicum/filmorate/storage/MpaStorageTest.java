package ru.yandex.practicum.filmorate.storage;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaStorageTest {
    MpaDbStorage mpaStorage;

    @Test
    public void testFindGenre() {
        assertThat(mpaStorage.findMpa(1L))
                .isInstanceOf(Mpa.class)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "G");;
    }

    @Test
    public void testFindAll() {
        assertThat(mpaStorage.findAll()).isNotEmpty()
                .hasSize(5)
                .filteredOn("name", "PG-13")
                .isNotEmpty()
                .hasExactlyElementsOfTypes(Mpa.class);
    }

    @Test
    public void testCreate() {
        Mpa newMpa = new Mpa();
        newMpa.setName("PG-18");
        newMpa.setDescription("Лицам после 18 лет просмотр разрешен");

        assertThat(mpaStorage.create(newMpa))
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 6L)
                .hasFieldOrPropertyWithValue("name", "PG-18");
    }

    @Test
    public void testUpdate() {
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
    public void testDelete() {
        assertThat(mpaStorage.delete(6L)).isFalse();
    }

}
