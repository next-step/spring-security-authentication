package nextstep.security.exception;

public class AuthenticationTokenException extends AuthenticationException {
    private static final String MESSAGE = "Token can not be decoded";

    public AuthenticationTokenException() {
        super(MESSAGE);
    }
}
