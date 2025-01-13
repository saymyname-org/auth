package ru.improve.unboundedSound.api.error;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpenfyException extends RuntimeException {

    private ErrorCode code;

    private String[] params = null;

    private Throwable cause = null;

    public OpenfyException(ErrorCode code) {
        this.code = code;
    }

    public OpenfyException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }

    public OpenfyException(ErrorCode code, String message, Throwable cause) {
        super(message);
        this.code = code;
        this.cause = cause;
    }

    public OpenfyException(ErrorCode code, Throwable cause) {
        this.code = code;
        this.cause = cause;
    }

    public OpenfyException(ErrorCode code, String message, String[] params) {
        super(message);
        this.code = code;
        this.params = params;
    }

    public OpenfyException(ErrorCode code, String[] params) {
        this.code = code;
        this.params = params;
    }
}
