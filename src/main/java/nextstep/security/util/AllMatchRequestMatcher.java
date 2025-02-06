package nextstep.security.util;

import jakarta.servlet.http.HttpServletRequest;

public class AllMatchRequestMatcher implements RequestMatcher {
    @Override
    public boolean matches(HttpServletRequest httpRequest) {
        return true;
    }
}
