package ru.improve.openfy.configuration.security;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Value
@ConfigurationProperties(prefix = "app.token", ignoreUnknownFields = false)
@Getter
@Setter
public class TokenConfiguration {

    private String secret;
}
