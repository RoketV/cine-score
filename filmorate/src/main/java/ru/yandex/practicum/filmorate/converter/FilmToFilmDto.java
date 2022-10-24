package ru.yandex.practicum.filmorate.mapper;


import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;

@Component
public class FilmToFilmDto implements Converter<Film, FilmDto> {

    @Override
    public FilmDto convert(Film film) {
        FilmDto filmDto = new FilmDto();
        filmDto.setDescription(filmDto.getDescription());
        filmDto.setName(film.getName());
        filmDto.setReleaseDate(film.getReleaseDate());
        filmDto.setDuration(film.getDuration());
        return filmDto;
    }
}
