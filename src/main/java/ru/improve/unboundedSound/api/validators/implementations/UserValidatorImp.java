package ru.improve.unboundedSound.api.validators.implementations;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import ru.improve.unboundedSound.api.dto.user.UserPatchRequest;
import ru.improve.unboundedSound.api.dto.user.UserPostRequest;
import ru.improve.unboundedSound.api.validators.OpenfyDtoValidator;
import ru.improve.unboundedSound.api.validators.UserValidator;

@Component
public class UserValidatorImp extends OpenfyDtoValidator implements UserValidator {

    @Override
    public boolean supports(Class<?> clazz) {
        return (clazz.equals(UserPostRequest.class) ||
                clazz.equals(UserPatchRequest.class));
    }

    @Override
    public void validate(Object target, Errors errors) {
        createAndThrowException(errors);
    }
}
