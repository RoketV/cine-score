package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

public interface FilmStorage {


    Film postFilm(FilmDto dto);
    Film updateFilm(FilmDto dto);
    Film deleteFilm(FilmDto dto);
    Map<Long, Film> getFilms();
    Film getFilm(long id);
}
