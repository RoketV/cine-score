package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exceptions.NoSuchEntityException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public List<UserDto> getUsers() {
        return userStorage.getUsers()
                .orElseThrow(() -> new NoSuchEntityException("there are no users in data base"))
                .values()
                .stream()
                .map(UserMapper.USER_MAPPER::toDto)
                .collect(Collectors.toList());
    }

    public UserDto getUser(long id) {
        return UserMapper.USER_MAPPER.toDto(userStorage.getUser(id)
                .orElseThrow(() -> new NoSuchEntityException("null instead of User")));
    }

    public UserDto postUser(UserDto dto) {
        return UserMapper.USER_MAPPER.toDto(userStorage.postUser(dto)
                .orElseThrow(() -> new NoSuchEntityException("User is null")));
    }

    public UserDto updateUser(UserDto dto) {
        return UserMapper.USER_MAPPER.toDto(userStorage.updateUser(dto)
                .orElseThrow(() -> new NoSuchEntityException("null instead of User")));
    }

    public UserDto addFriend(long userId, long friendId) {
        return UserMapper.USER_MAPPER.toDto(userStorage.addFriend(userId, friendId)
                .orElseThrow(() -> new NoSuchEntityException("cannot add friend to empty User")));
    }

    public UserDto deleteFriend(long userId, long friendId) {
        return UserMapper.USER_MAPPER.toDto(userStorage.deleteFriend(userId, friendId)
                .orElseThrow(() -> new NoSuchEntityException("cannot delete friend for empty User")));
    }

    public List<UserDto> getMutualFriends(long userId, long friendId) {
        return userStorage.getMutualFriends(userId, friendId)
                .orElseThrow(() -> new NoSuchEntityException("cannot get mutual friend for empty User"))
                .stream()
                .map(UserMapper.USER_MAPPER::toDto)
                .collect(Collectors.toList());
    }

    public List<UserDto> getFriends(long id) {
        return userStorage.getFriends(id)
                .orElseThrow(() -> new NoSuchEntityException("cannot get friends friend for empty User"))
                .stream()
                .map(UserMapper.USER_MAPPER::toDto)
                .collect(Collectors.toList());
    }
}
