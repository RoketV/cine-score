package ru.yandex.practicum.filmorate.storage.inMemory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;
@RequiredArgsConstructor
@Component
public class InMemoryGenreStorage implements GenreStorage {

    private final List<Genre> genres;

    @Override
    public Genre getGenre(int id) {
        return genres.get(id);
    }

    @Override
    public List<Genre> getGenres() {
        return genres;
    }
}
