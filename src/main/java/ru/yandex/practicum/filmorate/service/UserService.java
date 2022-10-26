package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exceptions.NoSuchEntityException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {


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
        if (noSuchUser(userId, friendId)) {
            log.warn("there is no such user within users");
            return new ResponseEntity<>("there is no such user", HttpStatus.NOT_FOUND);
        }
        if (alreadyFriends(userId, friendId)) {
            log.info("these users are already friends");
            return new ResponseEntity<>("you are friends already", HttpStatus.ALREADY_REPORTED);
        }
        Map<Long, User> users = userStorage.getUsers();
        User user = users.get(userId);
        User friend = users.get(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        return new ResponseEntity<>("friend added", HttpStatus.OK);
    }

    public ResponseEntity<String> deleteFriend(long userId, long friendId) {
        if (noSuchUser(userId, friendId)) {
            log.warn("there is no such user within users");
            return new ResponseEntity<>("there is no such user", HttpStatus.NOT_FOUND);
        }
        if (!alreadyFriends(userId, friendId)) {
            log.info("these users are not friends");
            return new ResponseEntity<>("you are not friends yet", HttpStatus.OK);
        }
        Map<Long, User> users = userStorage.getUsers();
        User user = users.get(userId);
        User friend = users.get(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        return new ResponseEntity<>("friend deleted", HttpStatus.OK);
    }

    public List<UserDto> getMutualFriends(long userId, long friendId) {
        if (noSuchUser(userId, friendId)) {
            log.warn("there is no such user within users");
            throw new NoSuchEntityException();
        }
        Set<Long> userFriends = userStorage.getUsers().get(userId).getFriends();
        Set<Long> secondUserFriends = userStorage.getUsers().get(friendId).getFriends();
        if (userFriends == null || secondUserFriends == null) {
            return new ArrayList<>();
        }
        Set<Long> common = new HashSet<>(userFriends);
        common.retainAll(secondUserFriends);
        if (common.isEmpty()) {
            log.info("users don's have mutual friends");
            return new ArrayList<>();
        } else {
            List<User> mutualFriends = new ArrayList<>();
            common.forEach(f -> mutualFriends.add(userStorage.getUsers().get(f)));
            return mutualFriends.stream().map(UserMapper.USER_MAPPER::toDto).collect(Collectors.toList());
        }
    }

    public List<UserDto> getFriends(long id) {
        if (noSuchUser(id)) {
            log.warn("there is no such user within users");
            throw new NoSuchEntityException();
        }
        List<User> friends = new ArrayList<>();
        userStorage.getUser(id).getFriends().forEach(i -> friends.add(userStorage.getUsers().get(i)));
        return friends.stream().map(UserMapper.USER_MAPPER::toDto).collect(Collectors.toList());
    }


    private boolean noSuchUser(Long... id) {
        Map<Long, User> users = userStorage.getUsers();
        return (users == null) || Arrays.stream(id).anyMatch(i -> (!users.containsKey(i)));
    }

    private boolean alreadyFriends(long userId, long friendId) {
        Map<Long, User> users = userStorage.getUsers();
        User user = users.get(userId);
        User friend = users.get(friendId);
        return (user != null && user.getFriends() != null && user.getFriends().contains(friendId))
                || (friend != null && friend.getFriends() != null && friend.getFriends().contains(userId));
    }
}
