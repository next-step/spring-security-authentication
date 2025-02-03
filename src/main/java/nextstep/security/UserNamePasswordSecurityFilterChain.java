package nextstep.security;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.filter.config.SecurityFilterChain;
import org.springframework.http.HttpMethod;

import java.util.Arrays;
import java.util.List;

public class UserNamePasswordSecurityFilterChain implements SecurityFilterChain {
    private final String[] requestURI;
    private final List<Filter> filters;

    public UserNamePasswordSecurityFilterChain(String[] requestURI, List<Filter> filters) {
        this.requestURI = requestURI;
        this.filters = filters;
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        boolean isMatchedURI = Arrays.stream(requestURI).anyMatch(uri -> uri.equals(request.getRequestURI()));
        boolean isPostMethod = HttpMethod.POST.name().equalsIgnoreCase(request.getMethod());
        return isMatchedURI && isPostMethod;
    }

    @Override
    public List<Filter> getFilters() {
        return filters;
    }
}