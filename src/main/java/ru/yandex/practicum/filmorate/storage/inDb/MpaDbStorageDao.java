package ru.yandex.practicum.filmorate.storage.inDb;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NoSuchEntityException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Primary
public class MpaDbStorageDao implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Mpa> getMpa(int id) {
        try {
            String sql = "SELECT * FROM MPA WHERE MPA_ID=?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, this::mapToMpa, id));
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchEntityException("there is no mpa with this index");
        }
    }

    @Override
    public Optional<List<Mpa>> getAllMpa() {
        String sql = "SELECT * FROM MPA";
        return Optional.of(jdbcTemplate.query(sql, this::mapToMpa));
    }

    private Mpa mapToMpa(ResultSet resultSet, int rows) throws SQLException {
        Mpa mpa = new Mpa();
        mpa.setId(resultSet.getInt("mpa_id"));
        mpa.setName(resultSet.getString("mpa"));
        return mpa;
    }
}
