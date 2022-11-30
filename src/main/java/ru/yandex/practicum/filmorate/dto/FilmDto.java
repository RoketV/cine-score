package ru.yandex.practicum.filmorate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.validator.BeforeFirstFilmValidation;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.LinkedHashSet;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
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
    private int rate;
    @NotNull
    private Mpa mpa;
    private LinkedHashSet<Genre> genres;
}
