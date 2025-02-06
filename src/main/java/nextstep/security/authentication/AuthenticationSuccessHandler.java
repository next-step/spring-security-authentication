package nextstep.security.authentication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthenticationSuccessHandler {
    void onAuthenticationSuccess(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
                                 Authentication authentication);
}
