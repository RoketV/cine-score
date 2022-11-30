package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.UserDto;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UserValidation {

    private static UserStorage userStorage;

    @Autowired
    public UserValidation(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public static void isValid(UserDto dto) {
        if (dto == null) {
            log.debug("problems while creating a user");
            throw new ValidationException("fail to create user");
        }
        if (dto.getName() == null || dto.getName().isBlank()) {
            dto.setName(dto.getLogin());
        }
        if (dto.getLogin() == null || dto.getLogin().isBlank()) {
            dto.setLogin(dto.getName());
        }
    }

    public static boolean doesExist(long... id) {
        List<Long> ids = userStorage.getUsers().values().stream()
                .map(User::getId)
                .collect(Collectors.toList());
        for (long l : id) {
            if (ids.contains(l)) {
                return true;
            }
        }
        return false;
    }
}
