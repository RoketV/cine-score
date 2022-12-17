package DbTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exceptions.NoSuchEntityException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import starter.FilmorateApplication;

import java.time.LocalDate;

@SpringBootTest(classes = FilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTests {

    private final FilmStorage filmStorage;
    private final MpaStorage mpaStorage;
    private Mpa mpa;
    private FilmDto dto;

    @BeforeEach
    public void config() {
        dto = (new FilmDto("name", " description",
                LocalDate.of(1996, 1, 28), 120));
        mpa = mpaStorage.getMpa(1).get();
        dto.setMpa(mpa);

    }

    @Test
    @Sql("classpath:data.sql")
    @DisplayName("tests post and get methods")
    public void testPostAndGetFilmMethod() {
        filmStorage.postFilm(dto);
        Film film = filmStorage.getFilm(1).get();
        Assertions.assertAll(
                () -> Assertions.assertEquals(film.getId(), 1),
                () -> Assertions.assertEquals(film.getName(), "name")
        );
    }

    @Test
    @Sql("classpath:data.sql")
    @DisplayName("tests that if we try to get non existing Film, method throws exception")
    public void testGetNotFoundFilmMethod() {
        Assertions.assertThrows(NoSuchEntityException.class, () -> filmStorage.getFilm(9999));
    }

    @Test
    @Sql("classpath:data.sql")
    @DisplayName("update film method check")
    public void updateFilmTest() {
        filmStorage.postFilm(dto);
        FilmDto newDto = new FilmDto("newName", " description",
                LocalDate.of(1996, 1, 28), 120);
        Mpa newMpa = mpaStorage.getMpa(2).get();
        newDto.setId(1);
        newDto.setMpa(newMpa);
        filmStorage.updateFilm(newDto);
        Film film = filmStorage.getFilm(1).get();
        Assertions.assertAll(
                () -> Assertions.assertEquals(film.getId(), 1),
                () -> Assertions.assertEquals(film.getName(), "newName"),
                () -> Assertions.assertEquals(film.getMpa(), newMpa)
        );
    }

    @Test
    @Sql("classpath:data.sql")
    @DisplayName("fail to update film method check")
    public void updateNonExistingFilmTest() {
        FilmDto newDto = new FilmDto("newName", " description",
                LocalDate.of(1996, 1, 28), 120);
        Mpa newMpa = mpaStorage.getMpa(2).get();
        newDto.setId(1);
        newDto.setMpa(newMpa);
        Assertions.assertThrows(NoSuchEntityException.class, () -> filmStorage.updateFilm(newDto));
    }
}
