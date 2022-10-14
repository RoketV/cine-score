package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.converter.UserDtoToUser;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exceptions.NoSuchEntityException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserService {

    private final Map<Long, User> users = new HashMap<>();

    public User postUser(UserDto dto) {
        Converter<UserDto, User> converter = new UserDtoToUser();
        if (dto.getName() == null) {
            dto.setName(dto.getLogin());
        }
        User user = converter.convert(dto);
        if (user != null) {
            users.put(user.getId(), user);
            log.info("film posted");
            return user;
        } else {
            log.debug("problems while creating a user");
            throw new ValidationException("fail to create user");
        }
    }

    public User updateUser(UserDto dto) {
        Converter<UserDto, User> converter = new UserDtoToUser();
        User user = converter.convert(dto);
        if (user == null) {
            log.warn("trying to make null entity");
            throw new ValidationException("entity cannot be null");
        }
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("film updated");
        } else {
            log.warn("cannot update non-existing entity");
            throw new NoSuchEntityException();
        }
        return user;
    }

    public void deleteUser(User user) {
        users.remove(user.getId());
    }

    public Map<Long, User> getUsers() {
        return users;
    }
}
