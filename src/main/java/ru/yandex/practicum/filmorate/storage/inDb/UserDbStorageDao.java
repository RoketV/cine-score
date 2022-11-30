package ru.yandex.practicum.filmorate.storage.inDb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exceptions.NoSuchEntityException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validator.UserValidation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Component("userDao")
@Primary
@Slf4j
public class UserDbStorageDao implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorageDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User postUser(UserDto dto) {
        UserValidation.isValid(dto);
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
        putLikedFilmsRecord(dto);

        return UserMapper.USER_MAPPER.toUser(dto);
    }

    @Override
    public User deleteUser(UserDto dto) {
        if (!UserValidation.doesExist(dto.getId())) {
            throw new NoSuchEntityException("there is no user with this id");
        }
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
        putLikedFilmsRecord(dto);
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
        throw new NoSuchEntityException("the table with users is empty");
    }

    @Override
    public User getUser(long id) {
        try {
            String sql = "SELECT * FROM users WHERE user_id=?";
            return jdbcTemplate.queryForObject(sql, this::mapToUser, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchEntityException("there is no such user with this id");
        }

    }

    @Override
    public ResponseEntity<String> deleteFriend(long userId, long friendId) {
        UserValidation.doesExist(userId, friendId);
        String sql = "DELETE FROM FRIENDSHIP_RELATIONSHIPS WHERE USER_ID=? AND FRIEND_ID=?";
        jdbcTemplate.update(sql, userId, friendId);
        return new ResponseEntity<>("friend added", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> addFriend(long userId, long friendId) {
        List<Long> ids = getUsers().values().stream()
                .map(User::getId)
                .collect(Collectors.toList());
        if (!(ids.contains(userId) && ids.contains(friendId))) {
            throw new NoSuchEntityException("there is no user with this id");
        }
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
                "WHERE USER_ID=?) AS O ON U.USER_ID=O.FRIEND_ID";

        return jdbcTemplate.query(sql, this::mapToUser, userId, friendId);
    }

    @Override
    public List<User> getFriends(long id) {
        String sql = "SELECT * FROM USERS AS U " +
                "INNER JOIN (SELECT FRIEND_ID FROM FRIENDSHIP_RELATIONSHIPS " +
                "WHERE USER_ID=?) AS FR on U.USER_ID = FR.FRIEND_ID";
        return jdbcTemplate.query(sql, this::mapToUser, id);
    }

    private void putLikedFilmsRecord(UserDto dto) {
        String sql = "INSERT INTO LIKED_FILMS VALUES ( ?,? )";
        Set<Long> likedFilms = dto.getLikedFilms();
        Optional.ofNullable(likedFilms)
                .ifPresent(lf -> lf.forEach(f -> jdbcTemplate.update(sql, dto.getId(), f)));
    }

    private User mapToUser(ResultSet resultSet, int rows) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("user_id"));
        user.setEmail(resultSet.getString("email"));
        user.setLogin(resultSet.getString("login"));
        user.setName(resultSet.getString("name"));
        user.setBirthday(resultSet.getDate("birthday").toLocalDate());
        setLikedFilm(user);
        return user;
    }

    private void setLikedFilm(User user) {
        String sql = "SELECT FILM_ID FROM LIKED_FILMS WHERE USER_ID=?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, user.getId());
        Set<Long> likedFilmId = new HashSet<>();
        while (rowSet.next()) {
            likedFilmId.add(rowSet.getLong("film_id"));
        }
        user.setLikedFilms(likedFilmId);
    }
}
