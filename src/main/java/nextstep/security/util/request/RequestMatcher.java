package nextstep.security.util.request;

import jakarta.servlet.http.HttpServletRequest;

public interface RequestMatcher {
    boolean matches(HttpServletRequest request);
}
