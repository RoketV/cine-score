package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserStorage {

    Optional<User> postUser(UserDto dto);

    Optional<User> deleteUser(UserDto dto);

    Optional<User> updateUser(UserDto dto);

    Optional<Map<Long, User>> getUsers();

    Optional<User> getUser(long id);

    Optional<User> deleteFriend(long userId, long friendId);

    Optional<User> addFriend(long userId, long friendId);

    Optional<List<User>> getMutualFriends(long userId, long friendId);

    Optional<List<User>> getFriends(long id);
}
