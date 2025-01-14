package ru.improve.openfy.api.validators.implementations;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import ru.improve.openfy.api.dto.auth.LoginRequest;
import ru.improve.openfy.api.validators.OpenfyDtoValidator;
import ru.improve.openfy.api.validators.AuthValidator;

@Component
public class AuthValidatorImp extends OpenfyDtoValidator implements AuthValidator {

    @Override
    public boolean supports(Class<?> clazz) {
        return (clazz.equals(LoginRequest.class));
    }

    @Override
    public void validate(Object target, Errors errors) {
        createAndThrowException(errors);
    }
}
