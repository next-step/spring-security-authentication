package nextstep.security.web.matcher;

import jakarta.servlet.http.HttpServletRequest;

public final class AnyRequestMatcher implements RequestMatcher {

    public static final RequestMatcher INSTANCE = new AnyRequestMatcher();

    @Override
    public boolean matches(HttpServletRequest request) {
        return true;
    }

}
