package nextstep.security.exception;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException() {}

    private AuthenticationException(String message) {
        super(message);
    }

    public static AuthenticationException invalidCredentials() {
        return SingletonHolder.INVALID_CREDENTIAL;
    }

    public static AuthenticationException notSupported() {
        return SingletonHolder.NOT_SUPPORTED;
    }

    public static AuthenticationException badToken() {
        return SingletonHolder.BAD_TOKEN;
    }

    private static final class SingletonHolder {
        private static final AuthenticationException INVALID_CREDENTIAL = new AuthenticationException(
                "Credential is invalid"
        );

        private static final AuthenticationException NOT_SUPPORTED = new AuthenticationException(
                "Authentication token is not supported by providers"
        );

        private static final AuthenticationException BAD_TOKEN = new AuthenticationException(
                "Token can not be decoded"
        );
    }
}
