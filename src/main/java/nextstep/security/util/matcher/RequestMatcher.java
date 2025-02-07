package nextstep.security.util.matcher;

import jakarta.servlet.http.HttpServletRequest;

public interface RequestMatcher {

    boolean matches(HttpServletRequest request);
}
