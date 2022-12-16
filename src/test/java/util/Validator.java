package util;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.constraints.NotBlank;
import java.util.Set;

public class Validator {

    private static final javax.validation.Validator VALIDATOR =
            Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> boolean hasErrorMessage(T dto, @NotBlank String message) {
        Set<ConstraintViolation<T>> errors = VALIDATOR.validate(dto);
        return errors.stream().map(ConstraintViolation::getMessage).anyMatch(message::equals);
    }
}
