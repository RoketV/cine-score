package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.service.FilmService;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public ResponseEntity<List<FilmDto>> getFilms() {
        return ResponseEntity.ok(filmService.getFilms());
    }

    @GetMapping("/popular")
    public ResponseEntity<List<FilmDto>> getPopularFilms(@RequestParam Optional<Integer> count) {
        return ResponseEntity.ok(filmService.getPopularFilms(count.orElse(10)));

    }

    @GetMapping("/{id}")
    public ResponseEntity<FilmDto> getFilm(@PathVariable long id) {
        return ResponseEntity.ok(filmService.getFilm(id));
    }

    @PostMapping
    public ResponseEntity<FilmDto> createFilm(@RequestBody @Valid FilmDto dto) {
        return ResponseEntity.ok(filmService.postFilm(dto));
    }

    @PutMapping
    public ResponseEntity<FilmDto> update(@RequestBody @Valid FilmDto dto) {
        return ResponseEntity.ok(filmService.putFilm(dto));
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
