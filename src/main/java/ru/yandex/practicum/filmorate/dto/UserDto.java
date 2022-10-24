package ru.yandex.practicum.filmorate.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.Set;

@Data
public class UserDto {

    public UserDto() {
    }

    public UserDto(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    private long id;
    @NotBlank
    @Email(message = "email has its structure")
    private String email;
    @NotBlank(message = "login cannot be empty or blank")
    private String login;
    private String name;
    @Past(message = "birthday cannot be in future")
    private LocalDate birthday;
    private Set<Long> likedFilms;

}
