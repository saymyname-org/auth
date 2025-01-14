package ru.improve.openfy.api.error;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceException extends RuntimeException {

    private ErrorCode code;

    private String[] params = null;

    private Throwable cause = null;

    public ServiceException(ErrorCode code) {
        this.code = code;
    }

    public ServiceException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }

    public ServiceException(ErrorCode code, String message, Throwable cause) {
        super(message);
        this.code = code;
        this.cause = cause;
    }

    public ServiceException(ErrorCode code, Throwable cause) {
        this.code = code;
        this.cause = cause;
    }

    public ServiceException(ErrorCode code, String message, String[] params) {
        super(message);
        this.code = code;
        this.params = params;
    }

    public ServiceException(ErrorCode code, String[] params) {
        this.code = code;
        this.params = params;
    }
}
