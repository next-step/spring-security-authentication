package nextstep.security.config;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public class DefaultSecurityFilterChain implements SecurityFilterChain {


    public <E> DefaultSecurityFilterChain(List<E> of) {
    }

    @Override
    public List<Filter> getFilters() {
        return List.of();
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return false;
    }

}
