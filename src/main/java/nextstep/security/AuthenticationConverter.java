package nextstep.security;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationConverter {

    Authentication convert(HttpServletRequest request);
}
