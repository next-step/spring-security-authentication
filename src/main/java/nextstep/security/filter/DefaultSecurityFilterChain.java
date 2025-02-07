package nextstep.security.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.util.request.RequestMatcher;

import java.util.ArrayList;
import java.util.List;

public class DefaultSecurityFilterChain implements SecurityFilterChain {

    private final List<Filter> filters;
    private final RequestMatcher requestMatcher;

    public DefaultSecurityFilterChain(final List<? extends Filter> filters, final RequestMatcher requestMatcher) {
        this.filters = new ArrayList<>(filters);
        this.requestMatcher = requestMatcher;
    }

    @Override
    public List<Filter> getFilters() {
        return this.filters;
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return requestMatcher.matches(request);
    }

}
