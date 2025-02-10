package nextstep.authentication.util.matcher;

import jakarta.servlet.http.HttpServletRequest;

public class AnyRequestMatcher implements RequestMatcher {

    public static final AnyRequestMatcher INSTANCE = new AnyRequestMatcher();

    private AnyRequestMatcher() {
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return true;
    }
}
