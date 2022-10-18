package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;

    @GetMapping
    public List<User> getUsers() {
        return new ArrayList<>(userService.getUsers().values());
    }

    @PostMapping
    public User createUser(@RequestBody @Valid UserDto dto) {
        return userService.postUser(dto);
    }

    @PutMapping
    public User update(@RequestBody @Valid UserDto dto) {
        return userService.updateUser(dto);
    }
}
