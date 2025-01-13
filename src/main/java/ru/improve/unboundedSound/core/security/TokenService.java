package ru.improve.unboundedSound.core.security;

import org.springframework.security.oauth2.jwt.Jwt;
import ru.improve.unboundedSound.core.models.Session;

public interface TokenService {

    Jwt generateToken(String login, Session session);

    long getSessionId(Jwt jwt);

    Jwt parseJwt(String jwt);
}
