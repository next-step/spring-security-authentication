package nextstep.security.util.request;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;

public class HttpRequestMatcher implements RequestMatcher {
    private final HttpMethod httpMethod;
    private final String url;

    public HttpRequestMatcher(final HttpMethod httpMethod, final String url) {
        this.httpMethod = httpMethod;
        this.url = url;
    }

    @Override
    public boolean matches(final HttpServletRequest request) {
        return url.equals(request.getRequestURI()) && httpMethod.matches(request.getMethod());
    }
}
