package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.UserDto;

import ru.yandex.practicum.filmorate.mapper.UserMapper;

import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {


    @Qualifier("inMemoryUser")
    private final UserStorage userStorage;

    public List<UserDto> getUsers() {
        return new ArrayList<>(userStorage.getUsers().values())
                .stream()
                .map(UserMapper.USER_MAPPER::toDto)
                .collect(Collectors.toList());
    }

    public UserDto getUser(long id) {
        return UserMapper.USER_MAPPER.toDto(userStorage.getUser(id));
    }

    public UserDto postUser(UserDto dto) {
        return UserMapper.USER_MAPPER.toDto(userStorage.postUser(dto));
    }

    public UserDto updateUser(UserDto dto) {
        return UserMapper.USER_MAPPER.toDto(userStorage.updateUser(dto));
    }

    public ResponseEntity<String> addFriend(long userId, long friendId) {

        return userStorage.addFriend(userId, friendId);
    }

    public ResponseEntity<String> deleteFriend(long userId, long friendId) {

        return userStorage.deleteFriend(userId, friendId);
    }

    public List<UserDto> getMutualFriends(long userId, long friendId) {

        return userStorage.getMutualFriends(userId, friendId)
                .stream()
                .map(UserMapper.USER_MAPPER::toDto)
                .collect(Collectors.toList());
    }

    public List<UserDto> getFriends(long id) {

        return userStorage.getFriends(id)
                .stream()
                .map(UserMapper.USER_MAPPER::toDto)
                .collect(Collectors.toList());
    }

}
