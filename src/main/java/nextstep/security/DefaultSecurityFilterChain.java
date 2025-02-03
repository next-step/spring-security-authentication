package nextstep.security;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public class DefaultSecurityFilterChain implements SecurityFilterChain {
    private final List<Filter> filters;

    public DefaultSecurityFilterChain(final List<Filter> filters) {
        this.filters = filters;
    }

    @Override
    public boolean match(final HttpServletRequest request) {
        if (isURINotEmpty(request)) {
            return true;
        }

        return false;
    }

    private static boolean isURINotEmpty(final HttpServletRequest request) {
        return !request.getRequestURI().isEmpty();
    }

    @Override
    public List<Filter> filters() {
        return this.filters;
    }
}
