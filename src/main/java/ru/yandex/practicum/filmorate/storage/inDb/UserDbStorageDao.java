package ru.yandex.practicum.filmorate.storage.inDb;

import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exceptions.NoSuchEntityException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;


@Component("userDao")
@Primary
public class UserDbStorageDao implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorageDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User postUser(UserDto dto) {
        String sql = "INSERT INTO users(email, login, name, birthday)" +
                " VALUES(?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(sql, new String[]{"user_id"});
            stmt.setString(1, dto.getEmail());
            stmt.setString(2, dto.getLogin());
            stmt.setString(3, dto.getName());
            stmt.setDate(4, java.sql.Date.valueOf(dto.getBirthday()));
            return stmt;
        }, keyHolder);
        dto.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return UserMapper.USER_MAPPER.toUser(dto);
    }

    @Override
    public User deleteUser(UserDto dto) {
        String sql = "DELETE FROM users WHERE USER_ID=?";
        jdbcTemplate.update(sql, dto.getId());
        return UserMapper.USER_MAPPER.toUser(dto);
    }

    @Override
    public User updateUser(UserDto dto) throws NoSuchEntityException {
        getUser(dto.getId());
        String sql = "UPDATE users " +
                "SET EMAIL=?, LOGIN=?, NAME=?, BIRTHDAY=? " +
                "WHERE USER_ID=?";
        jdbcTemplate.update(sql, dto.getEmail(), dto.getLogin(), dto.getName(), dto.getBirthday(), dto.getId());
        return UserMapper.USER_MAPPER.toUser(dto);
    }

    @Override
    public Map<Long, User> getUsers() {
        String sql = "SELECT * FROM users";
        List<User> users = jdbcTemplate.query(sql, this::mapToUser);
        if (!users.isEmpty()) {
            return users.stream()
                    .collect(Collectors.toMap(User::getId, Function.identity()));
        }
        throw new NoSuchEntityException();
    }

    @Override
    public User getUser(long id) {
        try {
            String sql = "SELECT * FROM users WHERE user_id=?";
            return jdbcTemplate.queryForObject(sql, this::mapToUser, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchEntityException();
        }

    }

    @Override
    public ResponseEntity<String> deleteFriend(long userId, long friendId) {
        return null;
    }

    @Override
    public ResponseEntity<String> addFriend(long userId, long friendId) {
        String sql = "INSERT INTO FRIENDSHIP_RELATIONSHIPS(user_id, friend_id) VALUES ( ?, ? )";
        jdbcTemplate.update(sql, userId, friendId);
        return new ResponseEntity<>("friend added", HttpStatus.OK);
    }

    @Override
    public List<User> getMutualFriends(long userId, long friendId) {
        String sql = "SELECT * FROM USERS AS U " +
                "INNER JOIN (SELECT FRIEND_ID FROM FRIENDSHIP_RELATIONSHIPS " +
                "WHERE USER_ID=?) AS FR on U.USER_ID = FR.FRIEND_ID " +
                "INNER JOIN (SELECT FRIEND_ID FROM FRIENDSHIP_RELATIONSHIPS " +
                "WHERE FRIEND_ID=?) AS O ON U.USER_ID=O.FRIEND_ID";

        return jdbcTemplate.query(sql, this::mapToUser, userId, friendId);
    }

    @Override
    public List<User> getFriends(long id) {
        String sql = "SELECT * FROM USERS AS U " +
                "INNER JOIN (SELECT FRIEND_ID FROM FRIENDSHIP_RELATIONSHIPS " +
                "WHERE USER_ID=?) AS FR on U.USER_ID = FR.FRIEND_ID";
        List<User> friends = jdbcTemplate.query(sql, this::mapToUser, id);
        return friends;
    }

    private User mapToUser(ResultSet resultSet, int rows) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("user_id"));
        user.setEmail(resultSet.getString("email"));
        user.setLogin(resultSet.getString("login"));
        user.setName(resultSet.getString("name"));
        user.setBirthday(resultSet.getDate("birthday").toLocalDate());
        return user;
    }
}
