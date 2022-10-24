package ru.yandex.practicum.filmorate.validator.validationTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.validator.util.Validator;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Random;

public class FilmValidationTest {

    private final static String FIRST_FILM_DATE = "1895-12-28";

    private String buildString(int size) {
        Random random = new Random();
        char letter = (char)(random.nextInt(26) + 'a');
        char[] chars = new  char[size];
        Arrays.fill(chars, letter);
        return new String(chars);
    }

    @Test
    @DisplayName("asserts that the name cannot be null")
    void nullNameFailTest() {
        FilmDto filmDto = new FilmDto(null, "description", LocalDate.now(), 120);
        Assertions.assertTrue(Validator.hasErrorMessage(filmDto, "name cannot be empty or blank"));
    }

    @Test
    @DisplayName("not empty name test")
    void notEmptyNameTest() {
        FilmDto filmDto = new FilmDto("name", "description", LocalDate.now(), 120);
        Assertions.assertFalse(Validator.hasErrorMessage(filmDto, "name cannot be empty or blank"));
    }

    @ParameterizedTest
    @DisplayName("asserts that the name cannot be blank")
    @ValueSource(strings = {" ", "  ", "   ", "    "})
    void blankNameTest(String name) {
        FilmDto filmDto = new FilmDto(name, "description", LocalDate.now(), 120);
        Assertions.assertTrue(Validator.hasErrorMessage(filmDto, "name cannot be empty or blank"));
    }

    @ParameterizedTest
    @DisplayName("asserts that description cannot be more then 200 chars")
    @ValueSource(ints = {201, 250} )
    void hugeDescriptionTest(int size) {
        FilmDto filmDto = new FilmDto("name", buildString(size), LocalDate.now(), 120);
        Assertions.assertTrue(Validator.hasErrorMessage(filmDto, "size cannot be more then 200 char"));
    }

    @ParameterizedTest
    @DisplayName("huge description test")
    @ValueSource(ints = {200, 150} )
    void blankNameTest(int size) {
        FilmDto filmDto = new FilmDto("name", buildString(size), LocalDate.now(), 120);
        Assertions.assertFalse(Validator.hasErrorMessage(filmDto, "size cannot be more then 200 char"));
    }

    @Test
    @DisplayName("checks negative duration")
    void negativeDurationTest() {
        FilmDto filmDto = new FilmDto("name", "description", LocalDate.now(), -120);
        Assertions.assertTrue(Validator.hasErrorMessage(filmDto, "duration cannot be negative number"));
    }

    @Test
    @DisplayName("checks positive duration")
    void positiveDurationTest() {
        FilmDto filmDto = new FilmDto("name", "description", LocalDate.now(), 120);
        Assertions.assertFalse(Validator.hasErrorMessage(filmDto, "duration cannot be negative number"));
    }

    @Test
    @DisplayName("checks localDate before first film")
    void beforeFirstFilmLocalDateTest() {
        FilmDto filmDto = new FilmDto("name", "description",
                LocalDate.parse(FIRST_FILM_DATE).minusDays(1), 120);
        Assertions.assertTrue(Validator.hasErrorMessage(filmDto, "film cannot be made before 1895-12-28"));
    }

    @Test
    @DisplayName("checks localDate at the time of the first film")
    void atTheTimeOfFirstFilmLocalDateTest() {
        FilmDto filmDto = new FilmDto("name", "description",
                LocalDate.parse(FIRST_FILM_DATE), 120);
        Assertions.assertTrue(Validator.hasErrorMessage(filmDto, "film cannot be made before 1895-12-28"));
    }

    @Test
    @DisplayName("checks localDate after first film")
    void afterFirstFilmLocalDateTest() {
        FilmDto filmDto = new FilmDto("name", "description",
                LocalDate.parse(FIRST_FILM_DATE).plusDays(1), 120);
        Assertions.assertFalse(Validator.hasErrorMessage(filmDto, "film cannot be made before 1895-12-28"));
    }
}
