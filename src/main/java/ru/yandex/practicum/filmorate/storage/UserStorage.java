package ru.yandex.practicum.filmorate.storage;

import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {

    User postUser(UserDto dto);

    User deleteUser(UserDto dto);

    User updateUser(UserDto dto);

    Map<Long, User> getUsers();

    User getUser(long id);

    ResponseEntity<String> deleteFriend(long userId, long friendId);

    ResponseEntity<String> addFriend(long userId, long friendId);

    List<User> getMutualFriends(long userId, long friendId);

    List<User> getFriends(long id);
}
