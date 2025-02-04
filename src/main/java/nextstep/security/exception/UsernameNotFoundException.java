package nextstep.security.exception;

public class UsernameNotFoundException extends AuthenticationException {
    public UsernameNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
