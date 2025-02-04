package nextstep.security.exception;

public class AccessDeniedException extends AuthenticationException {
    public AccessDeniedException(String message) {
        super(message);
    }
}
