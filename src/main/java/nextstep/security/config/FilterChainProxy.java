package nextstep.security.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class FilterChainProxy extends GenericFilterBean {
    private final List<SecurityFilterChain> filterChains;

    public FilterChainProxy(List<SecurityFilterChain> filterChains) {
        this.filterChains = filterChains;
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        new VirtualFilterChain(chain, findFilters(
                (HttpServletRequest) request
        )).doFilter(request, response);
    }

    private List<Filter> findFilters(HttpServletRequest request) {
        return this.filterChains.stream().filter(
                chain -> chain.matches(request)
        ).findFirst().map(
                SecurityFilterChain::getFilters
        ).orElseGet(Collections::emptyList);
    }
}
