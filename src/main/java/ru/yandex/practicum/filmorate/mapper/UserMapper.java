package ru.yandex.practicum.filmorate.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;

@Mapper
public interface UserMapper {


    UserMapper USER_MAPPER = Mappers.getMapper(UserMapper.class);
    User toUser(UserDto dto);
    UserDto toDto(User user);
}
