package DbTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import starter.FilmorateApplication;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = FilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTests {

    private final UserStorage userStorage;

    @BeforeEach
    public void config() {
        userStorage.postUser(new UserDto("email", "login", "name",
                LocalDate.of(1996, 1, 28)));
    }

    @Test
    @Sql("classpath:data.sql")
    @DisplayName("tests getUser method")
    public void testFindUserById() {
        User user = userStorage.getUser(1).get();
        Assertions.assertAll(
                () -> Assertions.assertEquals(user.getId(), 1),
                () -> Assertions.assertEquals(user.getName(), "name")
        );
    }

    @Test
    @Sql("classpath:data.sql")
    @DisplayName("tests postUser method")
    public void testPostUser() {
        User newUser = userStorage.postUser(new UserDto("emailTest", "loginTest", "nameTest",
                LocalDate.of(1996, 2, 28))).get();
        Assertions.assertAll(
                () -> Assertions.assertEquals(2, newUser.getId()),
                () -> Assertions.assertEquals("nameTest", newUser.getName())
        );
    }

    @Test
    @Sql("classpath:data.sql")
    @DisplayName("tests updateUser method")
    public void testUpdateUser() {
        UserDto newUser = new UserDto("email@mail", "newLogin", "newName",
                LocalDate.of(1996, 1, 28));
        newUser.setId(1);
        userStorage.updateUser(newUser);
        User user = userStorage.getUser(1).get();
        Assertions.assertAll(
                () -> assertEquals(newUser.getEmail(), user.getEmail()),
                () -> assertEquals(newUser.getLogin(), user.getLogin()),
                () -> assertEquals(newUser.getName(), user.getName()),
                () -> assertEquals(newUser.getBirthday(), user.getBirthday())
        );
    }

    @Test
    @Sql("classpath:data.sql")
    @DisplayName("tests getUsers method")
    public void testGetUser() {
        userStorage.postUser(new UserDto("emailTest", "loginTest", "nameTest",
                LocalDate.of(1996, 2, 28)));
        Collection<User> users = userStorage.getUsers().get().values();
        Assertions.assertEquals(2, users.size());
    }
}
