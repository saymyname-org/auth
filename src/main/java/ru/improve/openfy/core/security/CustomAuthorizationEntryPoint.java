package ru.improve.openfy.core.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import ru.improve.openfy.api.error.ServiceException;

import java.io.IOException;

import static ru.improve.openfy.api.error.ErrorCode.UNAUTHORIZED;

@RequiredArgsConstructor
@Slf4j
@Component
public class CustomAuthorizationEntryPoint implements AuthenticationEntryPoint {

    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        ServiceException serviceException = new ServiceException(UNAUTHORIZED);
        handlerExceptionResolver.resolveException(
                request, response, null, serviceException
        );
    }
}
