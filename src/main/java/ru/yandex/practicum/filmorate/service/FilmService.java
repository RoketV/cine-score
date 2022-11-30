package ru.yandex.practicum.filmorate.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exceptions.NoSuchEntityException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;


    public List<FilmDto> getFilms() {
        return new ArrayList<>(filmStorage.getFilms().values())
                .stream()
                .map(FilmMapper.FILM_MAPPER::toDto)
                .collect(Collectors.toList());
    }

    public List<FilmDto> getPopularFilms(int limit) {
        List<Film> films = new ArrayList<>(filmStorage.getFilms().values());
        return films.stream()
                .sorted(Comparator.comparing(Film::getRate).reversed())
                .limit(limit)
                .map(FilmMapper.FILM_MAPPER::toDto)
                .collect(Collectors.toList());

    }

    public FilmDto getFilm(long id) {
        return FilmMapper.FILM_MAPPER.toDto(filmStorage.getFilm(id));
    }

    public FilmDto postFilm(FilmDto dto) {
        return FilmMapper.FILM_MAPPER.toDto(filmStorage.postFilm(dto));
    }

    public FilmDto putFilm(FilmDto dto) {
        return FilmMapper.FILM_MAPPER.toDto(filmStorage.updateFilm(dto));
    }

    public ResponseEntity<String> addLike(long filmId, long userId) {
        if (entitiesDontExist(filmId, userId)) {
            throw new NoSuchEntityException("trying to extract non existing entity in addLike method");
        }
        User user = userStorage.getUsers().get(userId);
        Film film = filmStorage.getFilm(filmId);
        if (user.getLikedFilms().contains(filmId)) {
            return new ResponseEntity<>(String.format("user %s liked %s film before", user.getName(), film.getName())
                    , HttpStatus.ALREADY_REPORTED);
        }
        film.setRate(film.getRate() + 1);
        user.getLikedFilms().add(filmId);
        filmStorage.updateFilm(FilmMapper.FILM_MAPPER.toDto(film));
        userStorage.updateUser(UserMapper.USER_MAPPER.toDto(user));
        return new ResponseEntity<>("like added", HttpStatus.OK);
    }

    public ResponseEntity<String> deleteLike(long filmId, long userId) {
        if (entitiesDontExist(filmId, userId)) {
            log.info("trying to extract non existing entity in addLike method");
            throw new NoSuchEntityException("trying to extract non existing entity in addLike method");
        }
        User user = userStorage.getUsers().get(userId);
        Film film = filmStorage.getFilm(filmId);
        if (!user.getLikedFilms().contains(filmId)) {
            return new ResponseEntity<>(String.format("user %s never liked %s film before", user.getName(), film.getName())
                    , HttpStatus.NOT_FOUND);
        }
        film.setRate(film.getRate() - 1);
        user.getLikedFilms().remove(filmId);
        filmStorage.updateFilm(FilmMapper.FILM_MAPPER.toDto(film));
        userStorage.updateUser(UserMapper.USER_MAPPER.toDto(user));
        return new ResponseEntity<>("like removed", HttpStatus.OK);
    }


    public ResponseEntity<List<Mpa>> getAllMpa() {
        return Optional
                .of(new ResponseEntity<>(mpaStorage.getAllMpa(), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<Mpa> getMpa(int id) {
        return Optional
                .of(new ResponseEntity<>(mpaStorage.getMpa(id), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<List<Genre>> genGenres() {
        return Optional
                .of(new ResponseEntity<>(genreStorage.getGenres(), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<Genre> getGenre(int id) {
        return Optional
                .of(new ResponseEntity<>(genreStorage.getGenre(id), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    private boolean entitiesDontExist(long filmId, long userId) {
        return (!filmStorage.getFilms().containsKey(filmId))
                || (!userStorage.getUsers().containsKey(userId));
    }
}
