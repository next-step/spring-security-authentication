package nextstep.security;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.filter.config.SecurityFilterChain;

import java.util.List;
import java.util.Objects;

public class UserNamePasswordSecurityFilterChain implements SecurityFilterChain {
    private final List<Filter> filters;

    public UserNamePasswordSecurityFilterChain(List<Filter> filters) {
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
