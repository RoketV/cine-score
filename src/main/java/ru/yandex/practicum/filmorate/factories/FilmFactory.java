package ru.yandex.practicum.filmorate.factories;

import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;

public class FilmFactory {

    private static int id = 0;

    public static Film createFilm(FilmDto dto) {
        id++;
        Film film = FilmMapper.FILM_MAPPER.toFilm(dto);
        film.setId(id);
        return film;
    }
}
