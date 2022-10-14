package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.converter.FilmDtoToFilm;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exceptions.NoSuchEntityException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class FilmService {
    private final Map<Long, Film> films = new HashMap<>();

    public Map<Long, Film> getFilms() {
        return films;
    }

    public Film postFilm(FilmDto dto) {
        Converter<FilmDto, Film> converter = new FilmDtoToFilm();
        Film film = converter.convert(dto);
        if (film == null) {
            log.warn("trying to make null entity");
            throw new ValidationException("entity cannot be null");
        }
        films.put(film.getId(), film);
        log.info("film posted");
        return film;
    }

    public Film updateFilm(FilmDto dto) {
        FilmDtoToFilm converter = new FilmDtoToFilm();
        Film film = converter.convert(dto);
        if (film == null) {
            log.warn("trying to make null entity");
            throw new ValidationException("entity cannot be null");
        }
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("film updated");
        } else {
            log.warn("cannot update non-existing entity");
            throw new NoSuchEntityException();
        }
        return film;
    }

    public void deleteFilm(Film film) {
        films.remove(film.getId());
    }
}
