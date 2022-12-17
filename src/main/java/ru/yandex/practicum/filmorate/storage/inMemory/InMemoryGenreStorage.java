package ru.yandex.practicum.filmorate.storage.inMemory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class InMemoryGenreStorage implements GenreStorage {

    private final List<Genre> genres;

    @Override
    public Optional<Genre> getGenre(int id) {
        return Optional.of(genres.get(id));
    }

    @Override
    public Optional<List<Genre>> getGenres() {
        return Optional.of(genres);
    }
}
