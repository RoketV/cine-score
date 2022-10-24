package ru.yandex.practicum.filmorate.fabrics;


import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

@Component
public class UserFabric {

    private static int id = 0;

    public static User createUser(UserDto dto) {
        id++;
        User user = UserMapper.USER_MAPPER.toUser(dto);
        user.setId(id);
        return user;
    }
}
