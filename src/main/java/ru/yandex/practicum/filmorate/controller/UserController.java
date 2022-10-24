package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {


    private final InMemoryUserStorage inMemoryUserStorage;
    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsers() {
        List<User> users = new ArrayList<>(inMemoryUserStorage.getUsers().values());
        return users.stream().map(UserMapper.USER_MAPPER::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable long id) {
        return UserMapper.USER_MAPPER.toDto(inMemoryUserStorage.getUser(id));
    }

    @PostMapping
    public UserDto createUser(@RequestBody @Valid UserDto dto) {
        return UserMapper.USER_MAPPER.toDto(inMemoryUserStorage.postUser(dto));
    }

    @PutMapping
    public UserDto update(@RequestBody @Valid UserDto dto) {
        return UserMapper.USER_MAPPER.toDto(inMemoryUserStorage.updateUser(dto));
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<String> addFriend(@PathVariable long id, @PathVariable long friendId) {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<String> deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping("{id}/friends")
    public List<UserDto> getFriends(@PathVariable long id) {
        List<User> users = userService.getFriends(id);
        return users.stream().map(UserMapper.USER_MAPPER::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<UserDto> getMutualFriends(@PathVariable long id, @PathVariable long otherId) {
        List<User> mutualFriends = userService.getMutualFriends(id, otherId);
        return mutualFriends.stream().map(UserMapper.USER_MAPPER::toDto).collect(Collectors.toList());
    }
}

