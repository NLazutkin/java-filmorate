package ru.yandex.practicum.filmorate.storage;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.director.DirectorDbStorage;


import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@ComponentScan("ru.yandex.practicum.filmorate")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DirectorStorageTest {
    DirectorDbStorage directorStorage;

    @Test
    public void testFindDirector() {
        assertThat(directorStorage.findDirector(1L))
                .isInstanceOf(Director.class)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "Джордж Лукас");
    }

    @Test
    public void findAll() {
        assertThat(directorStorage.findAll()).isNotEmpty()
                .hasSize(5)
                .filteredOn("name", "Фрэнк Дарабонт")
                .isNotEmpty()
                .hasExactlyElementsOfTypes(Director.class);
    }

    @Test
    public void create() {
        Director newDirector = new Director();
        newDirector.setName("Андрей Тарковский");

        assertThat(directorStorage.create(newDirector))
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 6L)
                .hasFieldOrPropertyWithValue("name", "Андрей Тарковский");
    }

    @Test
    public void update() {
        Director newDirector = new Director();
        newDirector.setId(1L);
        newDirector.setName("Джордж Лукас I");

        assertThat(directorStorage.update(newDirector))
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "Джордж Лукас I");
    }

    @Test
    public void delete() {
        assertThat(directorStorage.delete(5L)).isTrue();
    }
}
