package ru.improve.unboundedSound.core.security.services;

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
import ru.improve.unboundedSound.api.dto.auth.LoginRequest;
import ru.improve.unboundedSound.api.dto.auth.LoginResponse;
import ru.improve.unboundedSound.api.error.OpenfyException;
import ru.improve.unboundedSound.core.mappers.AuthMapper;
import ru.improve.unboundedSound.core.models.Session;
import ru.improve.unboundedSound.core.models.User;
import ru.improve.unboundedSound.core.security.AuthService;
import ru.improve.unboundedSound.core.security.TokenService;
import ru.improve.unboundedSound.core.services.SessionService;
import ru.improve.unboundedSound.core.services.UserService;

import static ru.improve.unboundedSound.api.error.ErrorCode.SESSION_IS_OVER;
import static ru.improve.unboundedSound.api.error.ErrorCode.UNAUTHORIZED;

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
            throw new OpenfyException(UNAUTHORIZED, ex);
        }

        User user = (User) authentication.getPrincipal();
        if (user == null) {
            throw new OpenfyException(UNAUTHORIZED);
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

            if (!sessionService.checkSessionEnable(tokenService.getSessionId(jwtToken))) {
                throw new OpenfyException(SESSION_IS_OVER);
            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(jwtToken.getSubject());
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            securityContext.setAuthentication(authentication);

            return true;
        } catch (OpenfyException ex) {
            securityContext.setAuthentication(null);
            SecurityContextHolder.clearContext();
            response.reset();

            OpenfyException exception = (ex.getCode() == SESSION_IS_OVER ?
                    new OpenfyException(SESSION_IS_OVER) :
                    new OpenfyException(UNAUTHORIZED));
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
