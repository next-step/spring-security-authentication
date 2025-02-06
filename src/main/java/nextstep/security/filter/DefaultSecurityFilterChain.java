package nextstep.security.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.util.RequestMatcher;

import java.util.List;

public class DefaultSecurityFilterChain implements SecurityFilterChain {

    private final List<Filter> filters;
    private final RequestMatcher requestMatcher;

    public DefaultSecurityFilterChain(RequestMatcher requestMatcher,
                                      List<Filter> filters) {
        this.filters = filters;
        this.requestMatcher = requestMatcher;
    }

    @Override
    public boolean matches(HttpServletRequest httpRequest) {
        return requestMatcher.matches(httpRequest);
    }

    @Override
    public List<Filter> getFilters() {
        return this.filters;
    }
}
