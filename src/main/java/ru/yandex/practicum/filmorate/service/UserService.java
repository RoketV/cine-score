package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NoSuchEntityException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {


    private final InMemoryUserStorage inMemoryUserStorage;


    public ResponseEntity<String> addFriend(long userId, long friendId) {
        if (noSuchUser(userId, friendId)) {
            log.warn("there is no such user within users");
            return new ResponseEntity<>("there is no such user", HttpStatus.NOT_FOUND);
        }
        if (alreadyFriends(userId, friendId)) {
            log.info("these users are already friends");
            return new ResponseEntity<>("you are friends already", HttpStatus.ALREADY_REPORTED);
        }
        Map<Long, User> users = inMemoryUserStorage.getUsers();
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
        Map<Long, User> users = inMemoryUserStorage.getUsers();
        User user = users.get(userId);
        User friend = users.get(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        return new ResponseEntity<>("friend deleted", HttpStatus.OK);
    }

    public List<User> getMutualFriends(long userId, long friendId) {
        if (noSuchUser(userId, friendId)) {
            log.warn("there is no such user within users");
            throw new NoSuchEntityException();
        }
        Set<Long> userFriends = inMemoryUserStorage.getUsers().get(userId).getFriends();
        Set<Long> secondUserFriends = inMemoryUserStorage.getUsers().get(friendId).getFriends();
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
            common.forEach(f -> mutualFriends.add(inMemoryUserStorage.getUsers().get(f)));
            return mutualFriends;
        }
    }

    public List<User> getFriends(long id) {
        if (noSuchUser(id)) {
            log.warn("there is no such user within users");
            throw new NoSuchEntityException();
        }
        List<User> friends = new ArrayList<>();
        inMemoryUserStorage.getUser(id).getFriends().forEach(i -> friends.add(inMemoryUserStorage.getUsers().get(i)));
        return friends;
    }


    private boolean noSuchUser(Long... id) {      // не понимаю улучшил ли я код или ухудшил этими проверками :)
        Map<Long, User> users = inMemoryUserStorage.getUsers();
        return (users == null) || Arrays.stream(id).anyMatch(i -> (!users.containsKey(i)));
    }

    private boolean alreadyFriends(long userId, long friendId) {  // вроде стало читабельнее, когда вынес всё сюда,
        Map<Long, User> users = inMemoryUserStorage.getUsers();
        User user = users.get(userId);                            // но не понимаю насколько это всё было рационально
        User friend = users.get(friendId);
        return (user != null && user.getFriends() != null && user.getFriends().contains(friendId))
                || (friend != null && friend.getFriends() != null && friend.getFriends().contains(userId));
    }
}
