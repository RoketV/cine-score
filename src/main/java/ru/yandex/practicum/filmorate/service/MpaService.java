package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.exceptions.NoSuchEntityException;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MpaService {

    private final MpaStorage mpaStorage;

    public List<MpaDto> getAllMpa() {
        return mpaStorage.getAllMpa()
                .orElseThrow(() -> new NoSuchEntityException("MPAs are empty"))
                .stream()
                .map(MpaMapper.MPA_MAPPER::toDto)
                .collect(Collectors.toList());
    }

    public MpaDto getMpa(int id) {
       return MpaMapper.MPA_MAPPER.toDto(mpaStorage.getMpa(id)
               .orElseThrow(() -> new NoSuchEntityException("MPA not found")));
    }
}
