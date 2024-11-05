package nextstep.security.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AuthErrorCodes {
    UNAUTHORIZED_LOGIN_REQUEST(HttpStatus.UNAUTHORIZED, "Username not exist or Wrong password"),
    WRONG_BASIC_TOKEN_FORMAT(HttpStatus.UNAUTHORIZED, "Wrong basic token format");

    private final HttpStatus statusCode;
    private final String message;

    AuthErrorCodes(HttpStatus statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
