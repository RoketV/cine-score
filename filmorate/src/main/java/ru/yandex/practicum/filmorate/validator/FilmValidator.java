package ru.yandex.practicum.filmorate.validator;

import org.springframework.stereotype.Component;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

@Component
public class FilmValidator implements ConstraintValidator<BeforeFirstFilmValidation, LocalDate> {

    private final static String FIRST_FILM_DATE = "1895-12-28";

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        LocalDate filmBirth = LocalDate.parse(FIRST_FILM_DATE);
        return date.isAfter(filmBirth);
    }
}
