package ru.yandex.practicum.filmorate.storage.inDb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exceptions.NoSuchEntityException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository("filmDao")
@Primary
@Slf4j
public class FilmDbStorageDao implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorageDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film postFilm(FilmDto dto) {
        String sql = "INSERT INTO FILMS(title, description, release_date, duration, rate)" +
                " VALUES(?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(sql, new String[]{"film_id"});
            stmt.setString(1, dto.getName());
            stmt.setString(2, dto.getDescription());
            stmt.setDate(3, java.sql.Date.valueOf(dto.getReleaseDate()));
            stmt.setInt(4, dto.getDuration());
            stmt.setInt(5, dto.getRate());
            return stmt;
        }, keyHolder);
        dto.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        putMpaFilmRecord(dto);
        setFilmDtoMpa(dto);
        putFilmGenresRecord(dto);
        setFilmGenres(dto);
        return FilmMapper.FILM_MAPPER.toFilm(dto);
    }

    @Override
    public Film updateFilm(FilmDto dto) {
        if (!getFilms().containsKey(dto.getId())) {
            throw new NoSuchEntityException("trying to update non existing film");
        }
        String sql = "UPDATE FILMS " +
                "SET TITLE=?, DESCRIPTION=?, RELEASE_DATE=?, DURATION=?, RATE=?" +
                "WHERE FILM_ID=?";
        Optional.ofNullable(dto)
                .ifPresent(f -> jdbcTemplate.update(sql, f.getName(), f.getDescription(),
                        f.getReleaseDate(), f.getDuration(), f.getRate(), f.getId()));

        String sqlMpa = "UPDATE MPA_FILM " +
                "SET MPA_ID=?" +
                "WHERE FILM_ID=?";
        Optional.ofNullable(dto.getMpa())
                .ifPresent(mpa -> jdbcTemplate.update(sqlMpa, mpa.getId(), dto.getId()));


        String sqlDeleteGenres = "DELETE FROM FILMS_GENRES WHERE FILM_ID=?";
        jdbcTemplate.update(sqlDeleteGenres, dto.getId());
        putFilmGenresRecord(dto);

        return FilmMapper.FILM_MAPPER.toFilm(dto);
    }

    @Override
    public Map<Long, Film> getFilms() {
        String sql = "SELECT F.*, M.* " +
                "FROM FILMS AS F " +
                "         INNER JOIN MPA AS M ON M.MPA_ID IN " +
                "    (SELECT MPA_ID FROM MPA_FILM WHERE MPA_FILM.FILM_ID = F.FILM_ID)";
        List<Film> films = jdbcTemplate.query(sql, this::mapToFilm);
        films.forEach(this::setFilmGenres);
        return films.stream().collect(Collectors.toMap(Film::getId, Function.identity()));
    }

    @Override
    public Film getFilm(long id) {
        String sql = "SELECT F.*, M.* " +
                "FROM FILMS AS F " +
                "         INNER JOIN MPA AS M ON M.MPA_ID IN " +
                "    (SELECT MPA_ID FROM MPA_FILM WHERE MPA_FILM.FILM_ID = ?) WHERE FILM_ID=?";
        if (!getFilms().containsKey(id)) {
            throw new NoSuchEntityException("there is no such entity with this id");
        }
        return jdbcTemplate.queryForObject(sql, this::mapToFilm, id, id);
    }

    private void putFilmGenresRecord(FilmDto dto) {
        String sql = "INSERT INTO FILMS_GENRES VALUES ( ?,? )";
        Set<Genre> genres = dto.getGenres();
        Optional.ofNullable(genres)
                .ifPresent(set -> set.forEach(g -> jdbcTemplate.update(sql, dto.getId(), g.getId())));
    }

    private void setFilmGenres(FilmDto dto) {
        String sql = "SELECT * FROM GENRES WHERE" +
                " GENRE_ID IN (SELECT GENRE_ID FROM FILMS_GENRES WHERE FILM_ID=?)";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, dto.getId());
        LinkedHashSet<Genre> genres = new LinkedHashSet<>();
        while (rowSet.next()) {
            Genre genre = new Genre(rowSet.getInt("genre_id"), rowSet.getString("genre_name"));
            genres.add(genre);
        }
        dto.setGenres(genres);
    }


    private void setFilmDtoMpa(FilmDto dto) {
        String sql = "SELECT * FROM MPA " +
                "WHERE MPA_ID IN (SELECT MPA_ID FROM MPA_FILM WHERE FILM_ID=?)";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, dto.getId());
        while (rowSet.next()) {
            Mpa mpa = new Mpa(rowSet.getInt("mpa_id"), rowSet.getString("mpa"));
            dto.setMpa(mpa);
        }
    }

    private void putMpaFilmRecord(FilmDto dto) {
        String sql = "INSERT INTO MPA_FILM(film_id, mpa_id) VALUES ( ?,? )";
        Mpa mpa = dto.getMpa();
        Optional.ofNullable(mpa)
                .ifPresent(m -> jdbcTemplate.update(sql, dto.getId(), m.getId()));
    }

    private void setFilmGenres(Film film) {
        String sql = "SELECT * FROM GENRES WHERE" +
                " GENRE_ID IN (SELECT GENRE_ID FROM FILMS_GENRES WHERE FILM_ID=?)";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, film.getId());
        LinkedHashSet<Genre> genres = new LinkedHashSet<>();
        while (rowSet.next()) {
            Genre genre = new Genre(rowSet.getInt("genre_id"), rowSet.getString("genre_name"));
            genres.add(genre);
        }
        film.setGenres(genres);
    }

    private Film mapToFilm(ResultSet resultSet, int rows) throws SQLException {
        Film film = new Film();
        Mpa mpa = new Mpa(resultSet.getInt("mpa_id"), resultSet.getString("mpa"));

        film.setId(resultSet.getInt("film_id"));
        film.setName(resultSet.getString("title"));
        film.setDescription(resultSet.getString("description"));
        film.setReleaseDate(resultSet.getDate("release_date").toLocalDate());
        film.setDuration(resultSet.getInt("duration"));
        film.setRate(resultSet.getInt("rate"));
        film.setMpa(mpa);
        setFilmGenres(film);
        return film;
    }
}
