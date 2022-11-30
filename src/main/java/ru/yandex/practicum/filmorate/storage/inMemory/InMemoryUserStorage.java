package ru.yandex.practicum.filmorate.storage.inMemory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.factories.UserFactory;
import ru.yandex.practicum.filmorate.exceptions.NoSuchEntityException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validator.UserValidation;

import java.util.*;

@Component("inMemoryUser")
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();


    @Override
    public User postUser(UserDto dto) {
        UserValidation.isValid(dto);
        User user = UserFactory.createUser(dto);
        users.put(user.getId(), user);
        log.info("film posted");
        return user;
    }

    public User updateUser(UserDto dto) {
        if(!UserValidation.doesExist(dto.getId())) {
            throw new NoSuchEntityException("there is no user with this id");
        }
            User user = UserMapper.USER_MAPPER.toUser(dto);
            users.put(dto.getId(), user);
            log.info("user updated");
            return user;
    }

    public User deleteUser(UserDto dto) {
        if(!UserValidation.doesExist(dto.getId())) {
            throw new NoSuchEntityException("there is no user with this id");
        }
        users.remove(dto.getId());
        return UserMapper.USER_MAPPER.toUser(dto);
    }

    public Map<Long, User> getUsers() {
        return users;
    }

    public User getUser(long id) {
        if(!UserValidation.doesExist(id)) {
            throw new NoSuchEntityException("there is no user with this id");
        }
        return users.get(id);
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
        User user = users.get(userId);
        User friend = users.get(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        return new ResponseEntity<>("friend deleted", HttpStatus.OK);
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
        User user = users.get(userId);
        User friend = users.get(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        return new ResponseEntity<>("friend added", HttpStatus.OK);
    }

    public List<User> getMutualFriends(long userId, long friendId) {
        if (noSuchUser(userId, friendId)) {
            log.warn("there is no such user within users");
            throw new NoSuchEntityException("there is no user with this id");
        }
        Set<Long> userFriends = users.get(userId).getFriends();
        Set<Long> secondUserFriends = users.get(friendId).getFriends();
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
            common.forEach(f -> mutualFriends.add(users.get(f)));
            return mutualFriends;
        }
    }

    private boolean noSuchUser(Long... id) {
        return Arrays.stream(id).anyMatch(i -> (!users.containsKey(i)));
    }

    private boolean alreadyFriends(long userId, long friendId) {
        User user = users.get(userId);
        User friend = users.get(friendId);
        return (user != null && user.getFriends() != null && user.getFriends().contains(friendId))
                || (friend != null && friend.getFriends() != null && friend.getFriends().contains(userId));
    }

    public List<User> getFriends(long id) {
        if (noSuchUser(id)) {
            log.warn("there is no such user within users");
            throw new NoSuchEntityException("there is no user with this id");
        }
        List<User> friends = new ArrayList<>();
        users.get(id).getFriends().forEach(i -> friends.add(users.get(i)));
        return friends;
    }
}
