package ru.yandex.practicum.filmorate.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.model.Genre;

@Mapper
public interface GenreMapper {

    GenreMapper GENRE_MAPPER = Mappers.getMapper(GenreMapper.class);

    Genre toGenre(GenreDto dto);
    GenreDto toDto(Genre genre);
}
