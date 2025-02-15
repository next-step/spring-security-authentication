package nextstep.authentication.exception;

public class ProviderNotFoundException extends AuthenticationException {

    public ProviderNotFoundException(String message) {
        super(message);
    }
}
