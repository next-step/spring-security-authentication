package nextstep.security.util.request;

import jakarta.servlet.http.HttpServletRequest;

public class AllPassRequestMatcher implements RequestMatcher {
    @Override
    public boolean matches(final HttpServletRequest request) {
        return true;
    }
}
