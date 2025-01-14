package ru.improve.openfy.api.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {

    private long id;

    private String accessToken;

    private Instant issuedAt;

    private Instant expiredAt;
}
