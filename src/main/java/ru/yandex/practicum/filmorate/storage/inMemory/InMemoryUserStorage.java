package ru.yandex.practicum.filmorate.storage.inMemory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exceptions.NoSuchEntityException;
import ru.yandex.practicum.filmorate.factories.UserFactory;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validator.UserValidation;

import java.util.*;

@Repository("inMemoryUser")
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();


    @Override
    public Optional<User> postUser(UserDto dto) {
        UserValidation.isValid(dto);
        User user = UserFactory.createUser(dto);
        users.put(user.getId(), user);
        log.info("film posted");
        return Optional.of(user);
    }

    public Optional<User> updateUser(UserDto dto) {
        if(!UserValidation.doesExist(dto.getId())) {
            throw new NoSuchEntityException("there is no user with this id");
        }
            User user = UserMapper.USER_MAPPER.toUser(dto);
            users.put(dto.getId(), user);
            log.info("user updated");
            return Optional.of(user);
    }

    public Optional<User> deleteUser(UserDto dto) {
        if(!UserValidation.doesExist(dto.getId())) {
            throw new NoSuchEntityException("there is no user with this id");
        }
        users.remove(dto.getId());
        return Optional.of(UserMapper.USER_MAPPER.toUser(dto));
    }

    public Optional<Map<Long, User>> getUsers() {
        return Optional.of(users);
    }

    public Optional<User> getUser(long id) {
        if(!UserValidation.doesExist(id)) {
            throw new NoSuchEntityException("there is no user with this id");
        }
        return Optional.of(users.get(id));
    }

    public Optional<User> deleteFriend(long userId, long friendId) {
        if (noSuchUser(userId, friendId)) {
            log.warn("there is no such user within users");
        }
        if (!alreadyFriends(userId, friendId)) {
            log.info("these users are not friends");
        }
        User user = users.get(userId);
        User friend = users.get(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        return Optional.of(user);
    }

    public Optional<User> addFriend(long userId, long friendId) {
        if (noSuchUser(userId, friendId)) {
            log.warn("there is no such user within users");
        }
        if (alreadyFriends(userId, friendId)) {
            log.info("these users are already friends");
        }
        User user = users.get(userId);
        User friend = users.get(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        return Optional.of(user);
    }

    public Optional<List<User>> getMutualFriends(long userId, long friendId) {
        if (noSuchUser(userId, friendId)) {
            log.warn("there is no such user within users");
            throw new NoSuchEntityException("there is no user with this id");
        }
        Set<Long> userFriends = users.get(userId).getFriends();
        Set<Long> secondUserFriends = users.get(friendId).getFriends();
        if (userFriends == null || secondUserFriends == null) {
            return Optional.of(new ArrayList<>());
        }
        Set<Long> common = new HashSet<>(userFriends);
        common.retainAll(secondUserFriends);
        if (common.isEmpty()) {
            log.info("users don's have mutual friends");
            return Optional.of(new ArrayList<>());
        } else {
            List<User> mutualFriends = new ArrayList<>();
            common.forEach(f -> mutualFriends.add(users.get(f)));
            return Optional.of(mutualFriends);
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

    public Optional<List<User>> getFriends(long id) {
        if (noSuchUser(id)) {
            log.warn("there is no such user within users");
            throw new NoSuchEntityException("there is no user with this id");
        }
        List<User> friends = new ArrayList<>();
        users.get(id).getFriends().forEach(i -> friends.add(users.get(i)));
        return Optional.of(friends);
    }
}
