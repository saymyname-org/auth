package ru.improve.unboundedSound.core.security.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;
import ru.improve.unboundedSound.api.error.OpenfyException;
import ru.improve.unboundedSound.core.models.Session;
import ru.improve.unboundedSound.core.security.TokenService;
import ru.improve.unboundedSound.util.MessageKeys;

import static ru.improve.unboundedSound.api.error.ErrorCode.ILLEGAL_VALUE;
import static ru.improve.unboundedSound.util.Constants.SESSION_ID_CLAIM;

@RequiredArgsConstructor
@Service
public class TokenServiceImp implements TokenService {

    private final JwtEncoder jwtEncoder;

    private final JwtDecoder jwtDecoder;

    @Override
    public Jwt generateToken(String login, Session session) {
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(session.getIssuedAt())
                .expiresAt(session.getExpiredAt())
                .subject(login)
                .claim(SESSION_ID_CLAIM, session.getId())
                .build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims));
    }

    @Override
    public long getSessionId(Jwt jwt) {
        return jwt.getClaim(SESSION_ID_CLAIM);
    }

    @Override
    public Jwt parseJwt(String jwt) {
        try {
            return jwtDecoder.decode(jwt);
        } catch (JwtException ex) {
            throw new OpenfyException(ILLEGAL_VALUE, MessageKeys.SESSION_TOKEN_INVALID);
        }
    }
}
