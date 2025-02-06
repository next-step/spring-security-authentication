package nextstep.security.util;

import jakarta.servlet.http.HttpServletRequest;

public class FormLoginRequestMatcher implements RequestMatcher {
    private static final String DEFAULT_FILTER_PROCESS_URL = "/login";

    @Override
    public boolean matches(HttpServletRequest httpRequest) {
        return httpRequest.getRequestURI().equals(DEFAULT_FILTER_PROCESS_URL)
                && httpRequest.getMethod().equals("POST");
    }
}
