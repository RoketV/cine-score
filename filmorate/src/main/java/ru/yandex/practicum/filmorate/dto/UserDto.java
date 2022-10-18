package ru.yandex.practicum.filmorate.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
public class UserDto {


    long id;
    @NotBlank
    @Email(message = "email has its structure")
    String email;
    @NotBlank(message = "login cannot be empty or blank")
    String login;
    String name;
    @Past(message = "birthday cannot be in future")
    LocalDate birthday;

    public UserDto(long id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public UserDto() {}
}
