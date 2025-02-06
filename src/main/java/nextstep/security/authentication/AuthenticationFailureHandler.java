package nextstep.security.authentication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.exception.AuthenticationException;

import java.io.IOException;

public interface AuthenticationFailureHandler {
    void onAuthenticationFailure(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
                                 AuthenticationException exception) throws IOException;
}
