package nextstep.security.core;

import lombok.RequiredArgsConstructor;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
public class DefaultSecurityFilterChain implements SecurityFilterChain{

    private final List<Filter> filters;
    @Override
    public boolean matches(HttpServletRequest request) {
        return true;
    }

    @Override
    public List<Filter> getFilters() {
        return filters;
    }
}
