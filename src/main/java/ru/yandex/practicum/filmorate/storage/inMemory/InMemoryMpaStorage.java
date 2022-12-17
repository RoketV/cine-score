package ru.yandex.practicum.filmorate.storage.inMemory;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class InMemoryMpaStorage implements MpaStorage {

    private final List<Mpa> mpa;

    public Optional<Mpa> getMpa(int id) {
        return Optional.ofNullable(mpa.get(id));
    }

    public Optional<List<Mpa>> getAllMpa(){
        return Optional.ofNullable(mpa);
    }
}
