package ru.yandex.practicum.filmorate.storage.inDb;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NoSuchEntityException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
@Primary
@RequiredArgsConstructor
public class GenresDbStorageDao implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;


    @Override
    public List<Genre> getGenres() {
        String sql = "SELECT * FROM GENRES";
        return jdbcTemplate.query(sql, this::mapToGenre);
    }

    @Override
    public Genre getGenre(int id) {
        try {
            return Optional.of(getGenres().get(id-1))
                    .orElse(null);
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchEntityException("there is no genre with this index");
        }
    }

    private Genre mapToGenre(ResultSet resultSet, int rows) throws SQLException {
        Genre genre = new Genre();

        genre.setId(resultSet.getInt("genre_id"));
        genre.setName(resultSet.getString("genre_name"));
        return genre;
    }
}
