package ru.yandex.practicum.filmorate.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.model.Mpa;

@Mapper
public interface MpaMapper {

    MpaMapper MPA_MAPPER = Mappers.getMapper(MpaMapper.class);

    Mpa toMpa(MpaDto dto);
    MpaDto toDto(Mpa mpa);
}
