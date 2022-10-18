package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public List<Film> getFilms() {
        return new ArrayList<>(filmService.getFilms().values());
    }

    @PostMapping
    public Film createFilm(@RequestBody @Valid FilmDto dto) {
        return filmService.postFilm(dto);
    }

    @PutMapping
    public Film update(@RequestBody @Valid FilmDto dto) {
        return filmService.updateFilm(dto);
    }
}
