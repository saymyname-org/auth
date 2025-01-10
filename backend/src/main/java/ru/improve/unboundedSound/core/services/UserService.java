package ru.improve.unboundedSound.core.services;

import ru.improve.unboundedSound.api.dto.user.UserDataResponse;
import ru.improve.unboundedSound.api.dto.user.UserPostRequest;
import ru.improve.unboundedSound.api.dto.user.UserPostResponse;
import ru.improve.unboundedSound.core.models.User;

public interface UserService {

    UserDataResponse getUserProfile();

    UserPostResponse createNewUser(UserPostRequest userPostRequest);

    User getCurrentUser();
}
