package nextstep.security;

public class ProviderNotFoundException extends AuthenticationException {

    public ProviderNotFoundException(String message) {
        super(message);
    }

}
