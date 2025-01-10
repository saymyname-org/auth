package ru.improve.unboundedSound.api.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.improve.unboundedSound.api.dto.user.UserDataResponse;
import ru.improve.unboundedSound.api.dto.user.UserPatchRequest;
import ru.improve.unboundedSound.api.dto.user.UserPostRequest;
import ru.improve.unboundedSound.api.dto.user.UserPostResponse;
import ru.improve.unboundedSound.api.validators.UserValidator;
import ru.improve.unboundedSound.core.services.UserService;

import static ru.improve.unboundedSound.api.Paths.PROFILE;
import static ru.improve.unboundedSound.api.Paths.USERS;

@RestController()
@RequestMapping(USERS)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final UserValidator userValidator;

    @GetMapping(PROFILE)
    public ResponseEntity<UserDataResponse> getPersonProfile() {

        UserDataResponse profileResponse = userService.getUserProfile();

        return new ResponseEntity<>(profileResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserPostResponse> createNewUser(@RequestBody @Valid UserPostRequest userPostRequest,
                                                          BindingResult bindingResult) {

        userValidator.validate(userPostRequest, bindingResult);

        UserPostResponse userPostResponse = userService.createNewUser(userPostRequest);
        return new ResponseEntity<>(userPostResponse, HttpStatus.OK);
    }

    @PatchMapping(PROFILE)
    public ResponseEntity<Void> patchUserByAuth(@RequestBody @Valid UserPatchRequest userPatchRequest,
                                                BindingResult bindingResult) {

        userValidator.validate(userPatchRequest, bindingResult);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUserByAuth() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
