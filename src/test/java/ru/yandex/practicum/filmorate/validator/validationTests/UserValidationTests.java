package ru.yandex.practicum.filmorate.validator.validationTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.validator.util.Validator;

import java.time.LocalDate;

public class UserValidationTests {

    private static final LocalDate DAY_OF_BIRTH = LocalDate.parse("1996-01-28");

    @Test
    @DisplayName("checks login for null")
    void nullLoginTest() {
        UserDto dto = new UserDto("email@email.com", null, "name", DAY_OF_BIRTH);
        Assertions.assertTrue(Validator.hasErrorMessage(dto, "login cannot be empty or blank"));
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "  ", "   "})
    @DisplayName("checks login for blanks")
    void blankLoginTest(String blank) {
        UserDto dto = new UserDto("email@email.com", blank, "name", DAY_OF_BIRTH);
        Assertions.assertTrue(Validator.hasErrorMessage(dto, "login cannot be empty or blank"));
    }

    @Test
    @DisplayName("checks login for nonNull")
    void nonNullLoginTest() {
        UserDto dto = new UserDto("email@email.com", "login", "name", DAY_OF_BIRTH);
        Assertions.assertFalse(Validator.hasErrorMessage(dto, "login cannot be empty or blank"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"email", "this-iswrong?email@"})
    @DisplayName("checks email")
    void invalidEmailTest(String email) {
        UserDto dto = new UserDto(email, "login", "name", DAY_OF_BIRTH);
        Assertions.assertTrue(Validator.hasErrorMessage(dto, "email has its structure"));
    }

    @Test
    @DisplayName("checks valid email")
    void validEmailTest() {
        UserDto dto = new UserDto("email@email.com", "login", "name", DAY_OF_BIRTH);
        Assertions.assertFalse(Validator.hasErrorMessage(dto, "login cannot be empty or blank"));
    }

    @Test
    @DisplayName("checks birthday in future")
    void futureBirthday() {
        UserDto dto = new UserDto("email@email.com", "login", "name",
                LocalDate.now().plusDays(1));
        Assertions.assertTrue(Validator.hasErrorMessage(dto, "birthday cannot be in future"));
    }
}
