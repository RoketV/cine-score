package mapperTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserMapperTests {


    @Test
    @DisplayName("checks if mapper converts User to UserDto")
    void UserToDtoMapperTest() {
        User user = new User();
        user.setName("name");
        user.setEmail("email@email.com");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(1996, 1,28));
        UserDto dto = UserMapper.USER_MAPPER.toDto(user);
        Assertions.assertAll(
                () -> Assertions.assertEquals(dto.getName(), user.getName()),
                () -> Assertions.assertEquals(dto.getLogin(), user.getLogin()),
                () -> Assertions.assertEquals(dto.getEmail(), user.getEmail()),
                () -> Assertions.assertEquals(dto.getBirthday(), user.getBirthday())
        );
    }

    @Test
    @DisplayName("checks if mapper converts UserDto to User")
    void UserDtoToUserMapperTest() {
        UserDto dto = new UserDto();
        dto.setName("name");
        dto.setEmail("email@email.com");
        dto.setLogin("login");
        dto.setBirthday(LocalDate.of(1996, 1,28));
        User user = UserMapper.USER_MAPPER.toUser(dto);
        Assertions.assertAll(
                () -> Assertions.assertEquals(dto.getName(), user.getName()),
                () -> Assertions.assertEquals(dto.getLogin(), user.getLogin()),
                () -> Assertions.assertEquals(dto.getEmail(), user.getEmail()),
                () -> Assertions.assertEquals(dto.getBirthday(), user.getBirthday())
        );
    }
}
