package ru.yandex.practicum.filmorate.mapper;

import org.springframework.core.convert.converter.Converter;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;

public class UserDtoToUser implements Converter<UserDto, User> {
    @Override
    public User convert(UserDto dto) {
        User user = new User();
        if (dto.getId() != 0) {
            user.setId(dto.getId());
        }
        user.setBirthday(dto.getBirthday());
        user.setName(dto.getName());
        user.setLogin(dto.getLogin());
        user.setEmail(dto.getEmail());
        return user;
    }
}
