package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.exceptions.NoSuchEntityException;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreStorage genreStorage;

    public List<GenreDto> genGenres() {
        return genreStorage.getGenres()
                .orElseThrow(() -> new NoSuchEntityException("Genres are empty"))
                .stream()
                .map(GenreMapper.GENRE_MAPPER::toDto)
                .collect(Collectors.toList());
    }

    public GenreDto getGenre(int id) {
        return GenreMapper.GENRE_MAPPER.toDto(genreStorage.getGenre(id)
                .orElseThrow(() -> new NoSuchEntityException("Genre not found")));
    }
}
