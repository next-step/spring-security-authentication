package nextstep.security.core;

import lombok.RequiredArgsConstructor;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class DefaultSecurityFilterChain implements SecurityFilterChain{

    private final String[] paths;
    private final List<Filter> filters;
    @Override
    public boolean matches(HttpServletRequest request) {
        return Arrays.stream(paths).anyMatch(path -> request.getPathInfo().matches(path));
    }

    @Override
    public List<Filter> getFilters() {
        return filters;
    }
}
