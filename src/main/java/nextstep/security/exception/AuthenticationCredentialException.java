package nextstep.security.exception;

public class AuthenticationCredentialException extends AuthenticationException {
    private static final String MESSAGE = "Credential is invalid";

    public AuthenticationCredentialException() {
        super(MESSAGE);
    }
}
