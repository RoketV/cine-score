package ru.yandex.practicum.filmorate.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exceptions.NoSuchEntityException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public List<FilmDto> getFilms() {
        Map<Long, Film> films = filmStorage.getFilms()
                .orElseThrow(() -> new NoSuchEntityException("films are empty"));
        return new ArrayList<>(films.values())
                .stream()
                .map(FilmMapper.FILM_MAPPER::toDto)
                .collect(Collectors.toList());
    }

    public List<FilmDto> getPopularFilms(int limit) {
        return new ArrayList<>(filmStorage.getFilms()
                .orElseThrow(() -> new NoSuchEntityException("films are empty"))
                .values())
                .stream()
                .sorted(Comparator.comparing(Film::getRate).reversed())
                .limit(limit)
                .map(FilmMapper.FILM_MAPPER::toDto)
                .collect(Collectors.toList());
    }

    public FilmDto getFilm(long id) {
        return FilmMapper.FILM_MAPPER.toDto(filmStorage.getFilm(id)
                .orElseThrow(() -> new NoSuchEntityException("there is no such entity")));
    }

    public FilmDto postFilm(FilmDto dto) {
        return FilmMapper.FILM_MAPPER.toDto(filmStorage.postFilm(dto)
                .orElseThrow(() -> new ValidationException("cannot make null film")));
    }

    public FilmDto putFilm(FilmDto dto) {
        return FilmMapper.FILM_MAPPER.toDto(filmStorage.updateFilm(dto)
                .orElseThrow(() -> new ValidationException("cannot make null film")));
    }

    public FilmDto addLike(long filmId, long userId) {
        if (entitiesDontExist(filmId, userId)) {
            throw new NoSuchEntityException("trying to extract non existing entity in addLike method");
        }
        User user = userStorage.getUsers().get().get(userId);
        Film film = filmStorage.getFilm(filmId).get();
        if (user.getLikedFilms().contains(filmId)) {
            log.warn("user {} liked {} film before", user.getName(), film.getName());
            return FilmMapper.FILM_MAPPER.toDto(film);
        }
        film.setRate(film.getRate() + 1);
        user.getLikedFilms().add(filmId);
        filmStorage.updateFilm(FilmMapper.FILM_MAPPER.toDto(film));
        userStorage.updateUser(UserMapper.USER_MAPPER.toDto(user));
        log.info("like added");
        return FilmMapper.FILM_MAPPER.toDto(film);
    }

    public FilmDto deleteLike(long filmId, long userId) {
        if (entitiesDontExist(filmId, userId)) {
            throw new NoSuchEntityException("trying to extract non existing entity in addLike method");
        }
        User user = userStorage.getUsers().get().get(userId);
        Film film = filmStorage.getFilm(filmId).get();
        if (!user.getLikedFilms().contains(filmId)) {
            log.warn("user {} never liked {} film before", user.getName(), film.getName());
            return FilmMapper.FILM_MAPPER.toDto(film);
        }
        film.setRate(film.getRate() - 1);
        user.getLikedFilms().remove(filmId);
        filmStorage.updateFilm(FilmMapper.FILM_MAPPER.toDto(film));
        userStorage.updateUser(UserMapper.USER_MAPPER.toDto(user));
        return FilmMapper.FILM_MAPPER.toDto(film);
    }

    private boolean entitiesDontExist(long filmId, long userId) {
        return (filmStorage.getFilms().isPresent()
                && userStorage.getUsers().isPresent()
                && !filmStorage.getFilms().get().containsKey(filmId))
                || (!userStorage.getUsers().get().containsKey(userId));
    }
}
