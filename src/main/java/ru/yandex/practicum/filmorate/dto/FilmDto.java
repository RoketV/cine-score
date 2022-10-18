package ru.yandex.practicum.filmorate.dto;

import lombok.Data;
import ru.yandex.practicum.filmorate.validator.BeforeFirstFilmValidation;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class FilmDto {

    private long id;
    @NotBlank(message = "name cannot be empty or blank")
    private String name;
    @Size(max = 200, message = "size cannot be more then 200 char")
    private String description;
    @BeforeFirstFilmValidation
    private LocalDate releaseDate;
    @Min(value = 0, message = "duration cannot be negative number")
    private int duration;

    public FilmDto(long id, String name, String description, LocalDate releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public FilmDto(){

    }
}
