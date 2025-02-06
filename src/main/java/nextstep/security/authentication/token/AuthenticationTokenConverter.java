package nextstep.security.authentication.token;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;

public interface AuthenticationTokenConverter {
    Authentication convert(HttpServletRequest request);

    boolean supports(HttpServletRequest request);
}
