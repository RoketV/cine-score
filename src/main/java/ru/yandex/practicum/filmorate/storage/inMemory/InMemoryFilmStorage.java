package ru.yandex.practicum.filmorate.storage.inMemory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exceptions.NoSuchEntityException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.factories.FilmFactory;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Map;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films;

    public Optional<Map<Long, Film>> getFilms() {
        return Optional.of(films);
    }

    public Optional<Film> postFilm(FilmDto dto) {
        Film film = FilmFactory.createFilm(dto);
        films.put(film.getId(), film);
        log.info("film posted");
        return Optional.of(film);
    }

    public Optional<Film> updateFilm(FilmDto dto) {
        Film film = FilmMapper.FILM_MAPPER.toFilm(dto);
        if (film == null) {
            log.warn("trying to make null entity");
            throw new ValidationException("entity cannot be null");
        }
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("film updated");
        } else {
            log.warn("cannot update non-existing entity");
            throw new NoSuchEntityException("cannot update non-existing entity");
        }
        return Optional.of(film);
    }

    public Optional<Film> getFilm(long id) {
        if (!films.containsKey(id)) {
            log.info("trying to get non existing film");
            throw new NoSuchEntityException("trying to get non existing film");
        }
        return Optional.of(films.get(id));
    }
}
