package nextstep.security.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.SecurityFilterChain;
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
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        List<Filter> filters = getMatchedFilters(request);

        if (filters.isEmpty()) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        VirtualFilterChain virtualFilterChain = new VirtualFilterChain(filterChain, filters);
        virtualFilterChain.doFilter(servletRequest, servletResponse);
    }

    private List<Filter> getMatchedFilters(HttpServletRequest request) {
        for (SecurityFilterChain chain : this.filterChains) {
            if (chain.matches(request)) {
                return chain.getFilters();
            }
        }

        return Collections.emptyList();
    }

}
