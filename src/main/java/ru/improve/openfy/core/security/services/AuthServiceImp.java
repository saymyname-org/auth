package ru.improve.openfy.core.security.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerExceptionResolver;
import ru.improve.openfy.api.dto.auth.LoginRequest;
import ru.improve.openfy.api.dto.auth.LoginResponse;
import ru.improve.openfy.api.error.ServiceException;
import ru.improve.openfy.core.mappers.AuthMapper;
import ru.improve.openfy.core.models.Session;
import ru.improve.openfy.core.models.User;
import ru.improve.openfy.core.security.AuthService;
import ru.improve.openfy.core.security.TokenService;
import ru.improve.openfy.core.services.SessionService;
import ru.improve.openfy.core.services.UserService;

import static ru.improve.openfy.api.error.ErrorCode.SESSION_IS_OVER;
import static ru.improve.openfy.api.error.ErrorCode.UNAUTHORIZED;

@Log4j2
@RequiredArgsConstructor
@Service
public class AuthServiceImp implements AuthService {

    private final AuthenticationManager authManager;

    private final SessionService sessionService;

    private final UserService userService;

    private final TokenService tokenService;

    private final UserDetailsService userDetailsService;

    private final HandlerExceptionResolver handlerExceptionResolver;

    private final AuthMapper authMapper;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (AuthenticationException ex) {
            throw new ServiceException(UNAUTHORIZED, ex);
        }

        User user = (User) authentication.getPrincipal();
        if (user == null) {
            throw new ServiceException(UNAUTHORIZED);
        }

        Session session = sessionService.create(user);
        Jwt accessTokenJwt = tokenService.generateToken(user.getEmail(), session);

        LoginResponse loginResponse = authMapper.sessionToLoginResponse(session);
        loginResponse.setAccessToken(accessTokenJwt.getTokenValue());
        return loginResponse;
    }

    @Override
    public boolean setAuthentication(HttpServletRequest request, HttpServletResponse response) {
        SecurityContext securityContext = SecurityContextHolder.getContext();

        Authentication auth = securityContext.getAuthentication();
        if (!(auth instanceof JwtAuthenticationToken)) {
            log.info("Not authenticated request: {}: {}", request.getMethod(), request.getRequestURL());
            return true;
        }

        try {
            Jwt jwtToken = (Jwt) auth.getPrincipal();

            if (!sessionService.checkSessionEnableById(tokenService.getSessionId(jwtToken))) {
                throw new ServiceException(SESSION_IS_OVER);
            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(jwtToken.getSubject());
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            securityContext.setAuthentication(authentication);

            return true;
        } catch (ServiceException ex) {
            securityContext.setAuthentication(null);
            SecurityContextHolder.clearContext();
            response.reset();

            ServiceException exception = (ex.getCode() == SESSION_IS_OVER ?
                    new ServiceException(SESSION_IS_OVER) :
                    new ServiceException(UNAUTHORIZED));
            handlerExceptionResolver.resolveException(request, response, null, exception);

            return false;
        }
    }

    @Transactional
    @Override
    public void logout() {
        User currentUser = userService.getCurrentUser();
        sessionService.setUserSessionDisable(currentUser);
    }
}
