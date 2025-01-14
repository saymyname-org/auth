package ru.improve.openfy.api.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.improve.openfy.api.dto.auth.LoginRequest;
import ru.improve.openfy.api.dto.auth.LoginResponse;
import ru.improve.openfy.api.validators.AuthValidator;
import ru.improve.openfy.core.security.AuthService;

import static ru.improve.openfy.api.Paths.LOGIN;
import static ru.improve.openfy.api.Paths.LOGOUT;
import static ru.improve.openfy.api.Paths.SESSIONS;

@RestController()
@RequestMapping(SESSIONS)
@RequiredArgsConstructor
public class AuthController {

    private final AuthValidator authValidator;

    private final AuthService authService;

    @PostMapping(LOGIN)
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest,
                                               BindingResult bindingResult) {

        authValidator.validate(loginRequest, bindingResult);

        return new ResponseEntity<>(authService.login(loginRequest), HttpStatus.OK);
    }

    @PostMapping(LOGOUT)
    public ResponseEntity<LoginResponse> logout() {
        authService.logout();
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
