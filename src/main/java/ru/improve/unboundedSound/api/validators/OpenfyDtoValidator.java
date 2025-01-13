package ru.improve.unboundedSound.api.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import ru.improve.unboundedSound.api.error.OpenfyException;

import static ru.improve.unboundedSound.api.error.ErrorCode.ILLEGAL_DTO_VALUE;

public abstract class OpenfyDtoValidator {

    protected void createAndThrowException(Errors errors) {
        if (errors.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            StringBuilder fieldsWithErrors = new StringBuilder();
            for (FieldError error : errors.getFieldErrors()) {
                fieldsWithErrors.append(error.getField())
                        .append(" ");

                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append("; ");
            }

            throw new OpenfyException(ILLEGAL_DTO_VALUE, null, new String[]{fieldsWithErrors.toString()});
        }
    }
}
