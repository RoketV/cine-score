package ru.yandex.practicum.filmorate.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;

@Mapper
public interface FilmMapper {

    FilmMapper FILM_MAPPER = Mappers.getMapper(FilmMapper.class);
    Film toFilm(FilmDto dto);
    FilmDto toDto(Film film);
}
