package ru.improve.openfy.core.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.improve.openfy.api.dto.auth.LoginRequest;
import ru.improve.openfy.api.dto.auth.LoginResponse;

public interface AuthService {

    boolean setAuthentication(HttpServletRequest request, HttpServletResponse response);

    LoginResponse login(LoginRequest loginRequest);

    void logout();
}
