package ru.yandex.practicum.filmorate.storage.inDb;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NoSuchEntityException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
@Primary
public class MpaDbStorageDao implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Mpa getMpa(int id) {
        try {
            return Optional.of(getAllMpa().get(id-1))
                    .orElse(null);
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchEntityException("there is no mpa with this index");
        }
    }

    @Override
    public List<Mpa> getAllMpa() {
        String sql = "SELECT * FROM MPA";
        return jdbcTemplate.query(sql, this::mapToMpa);
    }

    private Mpa mapToMpa(ResultSet resultSet, int rows) throws SQLException {
        Mpa mpa = new Mpa();

        mpa.setId(resultSet.getInt("mpa_id"));
        mpa.setName(resultSet.getString("mpa"));
        return mpa;
    }
}
