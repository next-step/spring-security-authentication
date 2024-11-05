package nextstep.security.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
public class AuthenticationException extends RuntimeException{
    private final HttpStatus status;

    public AuthenticationException(AuthErrorCodes exceptions) {
        super(exceptions.getMessage());
        this.status = exceptions.getStatusCode();
    }

    public Map<String, String> getResponseBody() {
        return Map.of("detail", this.getMessage());
    }
}
