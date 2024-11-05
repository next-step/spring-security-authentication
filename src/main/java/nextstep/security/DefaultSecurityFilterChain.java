package nextstep.security;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class DefaultSecurityFilterChain implements SecurityFilterChain {

    private final String path;
    private final List<Filter> filters;

    public DefaultSecurityFilterChain(String path, List<Filter> filters) {
        this.path = path;
        this.filters = filters;
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return request.getRequestURI().startsWith(path);
    }

    @Override
    public List<Filter> getFilters() {
        return filters;
    }
}
