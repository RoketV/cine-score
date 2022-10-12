package ru.yandex.practicum.filmorate.converter;

import org.springframework.core.convert.converter.Converter;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;

public class UserToUserDto implements Converter<User, UserDto> {

    @Override
    public UserDto convert(User user) {
        UserDto userDto = new UserDto();
        userDto.setBirthday(user.getBirthday());
        userDto.setName(user.getName());
        userDto.setLogin(user.getLogin());
        userDto.setEmail(userDto.getEmail());
        return userDto;
    }
}
