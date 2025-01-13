package ru.improve.unboundedSound.api.error;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.EnumMap;
import java.util.Map;

import static ru.improve.unboundedSound.api.error.ErrorCode.ALREADY_EXIST;
import static ru.improve.unboundedSound.api.error.ErrorCode.ILLEGAL_DTO_VALUE;
import static ru.improve.unboundedSound.api.error.ErrorCode.ILLEGAL_VALUE;
import static ru.improve.unboundedSound.api.error.ErrorCode.INTERNAL_SERVER_ERROR;
import static ru.improve.unboundedSound.api.error.ErrorCode.NOT_FOUND;
import static ru.improve.unboundedSound.api.error.ErrorCode.SESSION_IS_OVER;
import static ru.improve.unboundedSound.api.error.ErrorCode.UNAUTHORIZED;
import static ru.improve.unboundedSound.util.MessageKeys.INVALID_AUTHORIZATION;
import static ru.improve.unboundedSound.util.MessageKeys.SESSION_DISABLE;
import static ru.improve.unboundedSound.util.MessageKeys.TITLE_ENTITY_ALREADY_EXIST;
import static ru.improve.unboundedSound.util.MessageKeys.TITLE_ILLEGAL_DTO_VALUE;
import static ru.improve.unboundedSound.util.MessageKeys.TITLE_ILLEGAL_VALUE;
import static ru.improve.unboundedSound.util.MessageKeys.TITLE_INTERNAL_SERVER_ERROR;
import static ru.improve.unboundedSound.util.MessageKeys.TITLE_USER_NOT_FOUND;

@Log4j2
@RequiredArgsConstructor
@RestControllerAdvice
public class OpenfyExceptionHandler {

    private static EnumMap<ErrorCode, Pair<String, HttpStatus>> ERRORS_MAP;

    private final MessageSource messageSource;

    static {
        ERRORS_MAP = new EnumMap<>(ErrorCode.class);
        ERRORS_MAP.putAll(Map.of(
                ILLEGAL_VALUE, Pair.of(TITLE_ILLEGAL_VALUE, HttpStatus.BAD_REQUEST),
                ILLEGAL_DTO_VALUE, Pair.of(TITLE_ILLEGAL_DTO_VALUE, HttpStatus.BAD_REQUEST),
                ALREADY_EXIST, Pair.of(TITLE_ENTITY_ALREADY_EXIST, HttpStatus.BAD_REQUEST),
                NOT_FOUND, Pair.of(TITLE_USER_NOT_FOUND, HttpStatus.NOT_FOUND),
                UNAUTHORIZED, Pair.of(INVALID_AUTHORIZATION, HttpStatus.UNAUTHORIZED),
                SESSION_IS_OVER, Pair.of(SESSION_DISABLE, HttpStatus.UNAUTHORIZED),
                INTERNAL_SERVER_ERROR, Pair.of(TITLE_INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR)
        ));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseBody> handleException(Exception ex) {
        Pair<ErrorCode, String> resolvedException = resolveException(ex);
        ErrorCode errorCode = resolvedException.getFirst();

//        log.error(ex);

        return ResponseEntity.status(ERRORS_MAP.get(errorCode).getSecond())
                .body(ErrorResponseBody.builder()
                        .code(errorCode.getCode())
                        .message(resolvedException.getSecond())
                        .build());
    }

    private Pair<ErrorCode, String> resolveException(Exception ex) {
        Pair<ErrorCode, String> resolvedException;
        if (ex instanceof OpenfyException) {
            resolvedException = resolveOpenfyException((OpenfyException) ex);
        } else {
            resolvedException = resolveErrorCode(INTERNAL_SERVER_ERROR);
        }
        return resolvedException;
    }

    private Pair<ErrorCode, String> resolveOpenfyException(OpenfyException ex) {
        Pair<String, HttpStatus> errorPair = ERRORS_MAP.get(ex.getCode());
        StringBuilder messageBuild = new StringBuilder(buildMessage(errorPair.getFirst(), ex.getParams()));
        if (ex.getMessage() != null) {
            messageBuild.append(" " + ex.getMessage());
        }
        messageBuild.append(buildMessageFromOpenfyExceptions(ex));
        return Pair.of(ex.getCode(), messageBuild.toString());
    }

    private Pair<ErrorCode, String> resolveErrorCode(ErrorCode errorCode) {
        return Pair.of(
                errorCode,
                buildMessage(ERRORS_MAP.get(errorCode).getFirst(), null));
    }

    private String buildMessageFromOpenfyExceptions(OpenfyException ex) {
        int messageCount = 0;
        Throwable causeException = ex.getCause();
        StringBuilder messageBuilder = new StringBuilder();
        while (causeException != null) {
            if (causeException.getMessage() != null) {
                if (messageCount == 0) {
                    messageBuilder.append(": ");
                }
                messageBuilder
                        .append(buildMessage(causeException.getMessage(), ex.getParams()))
                        .append(" ");
            }
            causeException = causeException.getCause();
        }
        return messageBuilder.toString().strip();
    }

    private String buildMessage(String key, String[] params) {
        return messageSource.getMessage(
                key,
                params,
                key,
                LocaleContextHolder.getLocale()
        );
    }
}
