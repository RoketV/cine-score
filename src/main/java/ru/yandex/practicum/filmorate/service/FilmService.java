package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

@Service
public class FilmService {
    private final Map<Long, Film> films = new HashMap<>();

    public Map<Long, Film> getFilms() {
        return films;
    }

    public void addFilm(Film film) {
        films.put(film.getId(), film);
    }

    public void deleteFilm(Film film) {
        films.remove(film.getId());
    }
}
