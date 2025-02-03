package nextstep.security.login;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.AuthenticationEntrypoint;
import nextstep.security.exception.AuthenticationException;

import java.io.IOException;

public class UsernamePasswordAuthenticationEntrypoint implements AuthenticationEntrypoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }
}
