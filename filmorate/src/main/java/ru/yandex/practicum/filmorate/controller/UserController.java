package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exceptions.NoSuchEntityException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public List<User> getUsers() {
        return new ArrayList<>(userService.getUsers().values());
    }

    @PostMapping
    public User createUser(@RequestBody @Valid UserDto dto) {
        Converter<UserDto, User> converter = new UserDtoToUser();
        if (dto.getName() == null) {
            dto.setName(dto.getLogin());
        }
        User user = converter.convert(dto);
        if (user != null) {
            userService.addUser(user);
            log.info("film posted");
            return user;
        } else {
            log.debug("problems while creating a user");
            throw new ValidationException("fail to create user");
        }
    }

    @PutMapping
    public User update(@RequestBody @Valid UserDto dto) {
        Converter<UserDto, User> converter = new UserDtoToUser();
        User user = converter.convert(dto);
        if (user == null) {
            log.warn("trying to make null entity");
            throw new ValidationException("entity cannot be null");
        }
        if (userService.getUsers().containsKey(user.getId())) {
            userService.addUser(user);
            log.info("film updated");
        } else {
            log.warn("cannot update non-existing entity");
            throw new NoSuchEntityException();
        }
        return user;
    }
}
