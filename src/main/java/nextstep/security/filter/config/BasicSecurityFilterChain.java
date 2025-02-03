package nextstep.security.filter.config;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BasicSecurityFilterChain implements SecurityFilterChain {
    private final String[] requestURI;
    private final List<Filter> filters;

    public BasicSecurityFilterChain(String[] requestURI, List<Filter> filters) {
        this.requestURI = Objects.requireNonNull(requestURI);
        this.filters = Objects.requireNonNull(filters);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return Arrays.stream(requestURI).anyMatch(uri -> uri.equals(request.getRequestURI()));
    }

    @Override
    public List<Filter> getFilters() {
        return filters;
    }
}