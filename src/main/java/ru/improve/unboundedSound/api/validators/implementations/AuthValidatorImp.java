package ru.improve.unboundedSound.api.validators.implementations;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import ru.improve.unboundedSound.api.dto.auth.LoginRequest;
import ru.improve.unboundedSound.api.validators.OpenfyDtoValidator;
import ru.improve.unboundedSound.api.validators.AuthValidator;

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
