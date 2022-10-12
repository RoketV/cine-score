package ru.yandex.practicum.filmorate.converter;

import org.springframework.core.convert.converter.Converter;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;

public class FilmDtoToFilm implements Converter<FilmDto, Film> {

    @Override
    public Film convert(FilmDto dto) {
        Film film = new Film();
        if (dto.getId() != 0) {
            film.setId(dto.getId());
        }
        film.setReleaseDate(dto.getReleaseDate());
        film.setName(dto.getName());
        film.setDescription(dto.getDescription());
        film.setDuration(dto.getDuration());
        return film;
    }
}
