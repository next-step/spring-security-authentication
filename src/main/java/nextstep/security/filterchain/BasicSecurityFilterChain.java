package nextstep.security.filterchain;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Objects;

public class BasicSecurityFilterChain implements SecurityFilterChain {
    private final List<Filter> filters;

    public BasicSecurityFilterChain(List<Filter> filters) {
        this.filters = Objects.requireNonNull(filters);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return true;
    }

    @Override
    public List<Filter> getFilters() {
        return filters;
    }
}
