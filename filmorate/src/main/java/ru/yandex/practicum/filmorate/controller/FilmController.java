package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exceptions.NoSuchEntityException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getFilms() {
        return new ArrayList<>(filmService.getFilms().values());
    }

    @PostMapping
    public Film createFilm(@RequestBody @Valid FilmDto dto) {
        Converter<FilmDto, Film> converter = new FilmDtoToFilm();
        Film film = converter.convert(dto);
        if (film == null) {
            log.warn("trying to make null entity");
            throw new ValidationException("entity cannot be null");
        }
        filmService.addFilm(film);
        log.info("film posted");
        return film;
    }

    @PutMapping
    public Film update(@RequestBody @Valid FilmDto dto) {
        FilmDtoToFilm converter = new FilmDtoToFilm();
        Film film = converter.convert(dto);
        if (film == null) {
            log.warn("trying to make null entity");
            throw new ValidationException("entity cannot be null");
        }
        if (filmService.getFilms().containsKey(film.getId())) {
            filmService.addFilm(film);
            log.info("film updated");
        } else {
            log.warn("cannot update non-existing entity");
            throw new NoSuchEntityException();
        }
        return film;
    }
}
