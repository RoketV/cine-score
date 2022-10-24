package ru.yandex.practicum.filmorate.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NoSuchEntityException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final InMemoryUserStorage inMemoryUserStorage;

    public List<Film> getPopularFilms(int limit) {
        List<Film> films = new ArrayList<>(inMemoryFilmStorage.getFilms().values());
        return films.stream()
                .sorted(Comparator.comparing(Film::getRate).reversed())
                .limit(limit)
                .collect(Collectors.toList());

    }

    public ResponseEntity<String> addLike(long filmId, long userId) {
        if (entitiesDontExist(filmId, userId)) {
            log.info("trying to extract non existing entity in addLike method");
            throw new NoSuchEntityException();
        }
        User user = inMemoryUserStorage.getUsers().get(userId);
        Film film = inMemoryFilmStorage.getFilm(filmId);
        if (user.getLikedFilms().contains(filmId)) {
            return new ResponseEntity<>(String.format("user %s liked %s film before", user.getName(), film.getName())
                    , HttpStatus.ALREADY_REPORTED);
        }
        film.setRate(film.getRate() + 1);
        user.getLikedFilms().add(filmId);
        return new ResponseEntity<>("like added", HttpStatus.OK);
    }

    public ResponseEntity<String> deleteLike(long filmId, long userId) {
        if (entitiesDontExist(filmId, userId)) {
            log.info("trying to extract non existing entity in addLike method");
            throw new NoSuchEntityException();
        }
        User user = inMemoryUserStorage.getUsers().get(userId);
        Film film = inMemoryFilmStorage.getFilm(filmId);
        if (!user.getLikedFilms().contains(filmId)) {
            return new ResponseEntity<>(String.format("user %s never liked %s film before", user.getName(), film.getName())
                    , HttpStatus.NOT_FOUND);
        }
        film.setRate(film.getRate() - 1);
        user.getLikedFilms().remove(filmId);
        return new ResponseEntity<>("like removed", HttpStatus.OK);
    }

    private boolean entitiesDontExist(long filmId, long userId) {
        return (!inMemoryFilmStorage.getFilms().containsKey(filmId))
                || (!inMemoryUserStorage.getUsers().containsKey(userId));
    }
}
