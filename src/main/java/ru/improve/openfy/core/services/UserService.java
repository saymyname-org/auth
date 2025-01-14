package ru.improve.openfy.core.services;

import ru.improve.openfy.api.dto.user.UserDataResponse;
import ru.improve.openfy.api.dto.user.UserPostRequest;
import ru.improve.openfy.api.dto.user.UserPostResponse;
import ru.improve.openfy.core.models.User;

public interface UserService {

    UserDataResponse getUserProfile();

    UserPostResponse createNewUser(UserPostRequest userPostRequest);

    User getCurrentUser();
}
