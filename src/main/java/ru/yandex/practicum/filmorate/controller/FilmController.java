package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final FilmService filmService;

    @GetMapping
    public List<FilmDto> getFilms() {
        List<Film> films = new ArrayList<>(inMemoryFilmStorage.getFilms().values());
        return films.stream().map(FilmMapper.FILM_MAPPER::toDto).collect(Collectors.toList());
    }

    @GetMapping("/popular")
    public List<FilmDto> getPopularFilms(@RequestParam Optional<Integer> count) {
        List<Film> films = filmService.getPopularFilms(count.orElse(10));
        return films.stream().map(FilmMapper.FILM_MAPPER::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public FilmDto getFilm(@PathVariable long id) {
        return FilmMapper.FILM_MAPPER.toDto(inMemoryFilmStorage.getFilm(id));
    }

    @PostMapping
    public FilmDto createFilm(@RequestBody @Valid FilmDto dto) {
        Film film = inMemoryFilmStorage.postFilm(dto);
        return FilmMapper.FILM_MAPPER.toDto(film);
    }

    @PutMapping
    public FilmDto update(@RequestBody @Valid FilmDto dto) {
        return FilmMapper.FILM_MAPPER.toDto(inMemoryFilmStorage.updateFilm(dto));
    }

    @PutMapping("/{filmId}/like/{userId}")
    public ResponseEntity<String> addLike(@PathVariable long filmId, @PathVariable long userId) {
        return filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public ResponseEntity<String> deleteLike(@PathVariable long filmId, @PathVariable long userId) {
        return filmService.deleteLike(filmId, userId);
    }
}
