package ru.improve.unboundedSound.core.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.improve.unboundedSound.api.dto.auth.LoginRequest;
import ru.improve.unboundedSound.api.dto.auth.LoginResponse;

public interface AuthService {

    boolean setAuthentication(HttpServletRequest request, HttpServletResponse response);

    LoginResponse login(LoginRequest loginRequest);

    void logout();
}
