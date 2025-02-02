package nextstep.security.authentication.converter;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;

public interface AuthenticationConverter {
    Authentication convert(HttpServletRequest request);

    boolean supports(HttpServletRequest request);
}
