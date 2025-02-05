package nextstep.security.exception;

public class AuthenticationProviderException extends AuthenticationException {
    private static final String MESSAGE = "Authentication token is not supported by providers";

    public AuthenticationProviderException() {
        super(MESSAGE);
    }
}
