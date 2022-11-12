package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public interface UserStorage {

    User postUser(UserDto dto);

    User deleteUser(UserDto dto);

    User updateUser(UserDto dto);

    Map<Long, User> getUsers();

    User getUser(long id);
}
