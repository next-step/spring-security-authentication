package nextstep.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.exception.AuthenticationException;

import java.io.IOException;

public interface AuthenticationEntrypoint {

    void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException;
}
