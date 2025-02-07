package nextstep.security.filter.config;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.List;

public class DefaultSecurityFilterChain implements SecurityFilterChain {
    private final List<Filter> filters;

    public DefaultSecurityFilterChain(final Filter... filters) {
        this.filters = Arrays.asList(filters);
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
