package ru.yandex.practicum.filmorate.storage.inDb;

import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Map;

public class FilmDbStorageDao implements FilmStorage {
    @Override
    public Film postFilm(FilmDto dto) {
        return null;
    }

    @Override
    public Film updateFilm(FilmDto dto) {
        return null;
    }

    @Override
    public Film deleteFilm(FilmDto dto) {
        return null;
    }

    @Override
    public Map<Long, Film> getFilms() {
        return null;
    }

    @Override
    public Film getFilm(long id) {
        return null;
    }
}
