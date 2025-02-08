package nextstep.security.web;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.filter.VirtualFilterChain;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class FilterChainProxy extends GenericFilterBean {

    private final List<SecurityFilterChain> filterChains;

    public FilterChainProxy(final List<SecurityFilterChain> filterChains) {
        this.filterChains = filterChains;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if(servletRequest instanceof HttpServletRequest) {
            List<Filter> filters = this.filterChains
                    .stream()
                    .filter(chain -> chain.matches((HttpServletRequest) servletRequest))
                    .findFirst()
                    .map(SecurityFilterChain::getFilters)
                    .orElseGet(Collections::emptyList);

            new VirtualFilterChain(filters, filterChain)
                    .doFilter(servletRequest, servletResponse);
        }
    }
}
