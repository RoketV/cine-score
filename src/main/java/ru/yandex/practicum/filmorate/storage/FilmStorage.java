package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;
import java.util.Optional;

public interface FilmStorage {
    Optional<Film> postFilm(FilmDto dto);

    Optional<Film> updateFilm(FilmDto dto);

    Optional<Map<Long, Film>> getFilms();

    Optional<Film> getFilm(long id);
}
