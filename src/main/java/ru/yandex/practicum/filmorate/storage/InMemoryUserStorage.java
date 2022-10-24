package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.fabrics.UserFabric;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.exceptions.NoSuchEntityException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();



    @Override
    public User postUser(UserDto dto) {
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
        User user = UserFabric.createUser(dto);
            users.put(user.getId(), user);
            log.info("film posted");
            return user;
    }

    public User updateUser(UserDto dto) {
        if (dto == null) {
            log.warn("trying to make null entity");
            throw new ValidationException("entity cannot be null");
        }
        if (users.containsKey(dto.getId())) {
            User user = UserMapper.USER_MAPPER.toUser(dto);
            users.put(dto.getId(), user);
            log.info("user updated");
            return user;
        } else {
            log.warn("cannot update non-existing entity");
            throw new NoSuchEntityException();
        }
    }

    public User deleteUser(UserDto dto) {
        users.remove(dto.getId());
        return UserMapper.USER_MAPPER.toUser(dto);
    }

    public Map<Long, User> getUsers() {
        return users;
    }

    public User getUser(long id) {
        if (!users.containsKey(id)) {
            log.info("no user with this id");
            throw new NoSuchEntityException();
        }
        return users.get(id);
    }
}
