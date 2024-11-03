package nextstep.app.exception;

import org.springframework.http.HttpStatus;

public enum AuthErrorCodes {
    UNAUTHORIZED_LOGIN_REQUEST(HttpStatus.UNAUTHORIZED, "Username not exist or Wrong password"),
    WRONG_BASIC_TOKEN_FORMAT(HttpStatus.UNAUTHORIZED, "Wrong basic token format");

    private final HttpStatus statusCode;
    private final String message;

    AuthErrorCodes(HttpStatus statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }
}
