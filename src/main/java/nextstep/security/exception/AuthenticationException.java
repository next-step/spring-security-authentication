package nextstep.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
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

    private static class SingletonHolder {
        private static final AuthenticationException INVALID_CREDENTIAL = new AuthenticationException(
                "Credential is invalid"
        );

        private static final AuthenticationException NOT_SUPPORTED = new AuthenticationException(
                "Authentication token is not supported by providers"
        );
    }
}
