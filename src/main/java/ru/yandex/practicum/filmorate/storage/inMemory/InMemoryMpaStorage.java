package ru.yandex.practicum.filmorate.storage.inMemory;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@RequiredArgsConstructor
@Component
public class InMemoryMpaStorage implements MpaStorage {

    private final List<Mpa> mpa;

    public Mpa getMpa(int id) {
        return mpa.get(id);
    }

    public List<Mpa> getAllMpa(){
        return mpa;
    }
}
