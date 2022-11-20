package ru.yandex.practicum.filmorate.validator.mapperTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class FilmMapperTests {

    @Test
    @DisplayName("checks if mapper converts Film to FilmDto")
    void FilmToDtoMapperTest() {
        Film film = new Film();
        film.setTitle("name");
        film.setDescription("description");
        film.setDuration(120);
        film.setReleaseDate(LocalDate.of(1996, 1,28));
        FilmDto dto = FilmMapper.FILM_MAPPER.toDto(film);
        Assertions.assertAll(
                () -> Assertions.assertEquals(dto.getName(), film.getTitle()),
                () -> Assertions.assertEquals(dto.getDescription(), film.getDescription()),
                () -> Assertions.assertEquals(dto.getDuration(), film.getDuration()),
                () -> Assertions.assertEquals(dto.getReleaseDate(), film.getReleaseDate())
        );
    }

    @Test
    @DisplayName("checks if mapper converts FilmDto to Film")
    void DtoToDtoFilmTest() {
        FilmDto dto = new FilmDto();
        dto.setName("name");
        dto.setDescription("description");
        dto.setDuration(120);
        dto.setReleaseDate(LocalDate.of(1996, 1,28));
        Film film = FilmMapper.FILM_MAPPER.toFilm(dto);
        Assertions.assertAll(
                () -> Assertions.assertEquals(film.getTitle(), film.getTitle()),
                () -> Assertions.assertEquals(film.getDescription(), film.getDescription()),
                () -> Assertions.assertEquals(film.getDuration(), film.getDuration()),
                () -> Assertions.assertEquals(film.getReleaseDate(), film.getReleaseDate())
        );
    }
}
