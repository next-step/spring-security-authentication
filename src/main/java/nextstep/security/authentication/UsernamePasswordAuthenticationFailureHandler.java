package nextstep.security.authentication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.context.SecurityContextHolder;
import nextstep.security.exception.AuthenticationException;

import java.io.IOException;

public class UsernamePasswordAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
                                        AuthenticationException exception) throws IOException {

        SecurityContextHolder.clearContext();
        httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, exception.getMessage());
    }
}
