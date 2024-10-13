package ru.yandex.practicum.filmorate.storage;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.dto.Pair;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@ComponentScan("ru.yandex.practicum.filmorate")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserStorageTest {
    UserDbStorage userStorage;

    @Test
    public void testIsUserWithEmailExist() {
        assertThat(userStorage.isUserWithEmailExist("Capitan@yandex.ru")).isTrue();
    }

    @Test
    public void testFindUser() {
        assertThat(userStorage.findUser(1L))
                .isInstanceOf(User.class)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "Capitan")
                .hasFieldOrPropertyWithValue("email", "Capitan@yandex.ru");
    }

    @Test
    public void testGetUsers() {
        assertThat(userStorage.getUsers()).isNotEmpty()
                .hasSize(3)
                .filteredOn("name", "Sparrow")
                .isNotEmpty()
                .hasExactlyElementsOfTypes(User.class);
    }

    @Test
    public void testFindFriends() {
        assertThat(userStorage.findFriends(1L)).isNotEmpty()
                .hasSize(2)
                .isInstanceOf(Collection.class)
                .first()
                .extracting(User::getName)
                .isEqualTo("Jack");
    }

    @Test
    public void testAddFriend() {
        Pair<String, String> names = new Pair<>("Jack", "Sparrow");

        assertThat(userStorage.addFriend(2L, 3L))
                .isNotNull()
                .isInstanceOf(Pair.class)
                .isEqualTo(names);
    }

    @Test
    public void testDeleteFriend() {
        Pair<String, String> names = new Pair<>("Capitan", "Sparrow");

        assertThat(userStorage.deleteFriend(1L, 3L))
                .isNotNull()
                .isInstanceOf(Pair.class)
                .isEqualTo(names);
    }

    @Test
    public void testCreate() {
        User newUser = new User();
        newUser.setEmail("WillTurner@yandex.ru");
        newUser.setName("Will Turner");
        newUser.setLogin("Will Turner");
        newUser.setBirthday(LocalDate.of(2000, 1, 1));

        User createdUser = userStorage.create(newUser);

        assertThat(createdUser)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", createdUser.getId())
                .hasFieldOrPropertyWithValue("email", newUser.getEmail());
    }

    @Test
    public void testUpdate() {
        User newUser = new User();
        newUser.setId(3L);
        newUser.setEmail("TheSparrow@yandex.ru");
        newUser.setName("TheSparrow");
        newUser.setLogin("TheSparrow");
        newUser.setBirthday(LocalDate.of(2000, 1, 1));


        assertThat(userStorage.update(newUser))
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 3L)
                .hasFieldOrPropertyWithValue("name", "TheSparrow")
                .hasFieldOrPropertyWithValue("email", "TheSparrow@yandex.ru")
                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(2000, 1, 1));
    }

    @Test
    public void testDelete() {
        assertThat(userStorage.delete(3L)).isTrue();
    }

}
