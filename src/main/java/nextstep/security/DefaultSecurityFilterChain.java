package nextstep.security;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public class DefaultSecurityFilterChain implements SecurityFilterChain {

    private final List<Filter> filters;

    public DefaultSecurityFilterChain(List<Filter> filters) {
        this.filters = List.copyOf(filters);
    }

    @Override
    public List<Filter> getFilters() {
        return filters;
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return true;
    }
}
