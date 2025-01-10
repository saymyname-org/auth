package ru.improve.unboundedSound.api.error;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@NoArgsConstructor
@Builder
public class ErrorResponseBody {

    private int code;

    private String message;

    public ErrorResponseBody(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
