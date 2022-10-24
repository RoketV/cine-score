package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    private final Map<Long, User> users = new HashMap<>();

    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    public void deleteUser(User user) {
        users.remove(user.getId());
    }

    public Map<Long, User> getUsers() {
        return users;
    }
}
