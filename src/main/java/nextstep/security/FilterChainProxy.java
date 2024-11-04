package nextstep.security;

import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public class FilterChainProxy extends GenericFilterBean {
    private List<SecurityFilterChain> filterChains;

    public FilterChainProxy(List<SecurityFilterChain> filterChains) {
        this.filterChains = filterChains;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        List<Filter> filters = getFilters((HttpServletRequest) servletRequest);
        VirtualFilterChain virtualFilterChain = new VirtualFilterChain(filterChain, filters);
        virtualFilterChain.doFilter(servletRequest, servletResponse);
    }

    private List<Filter> getFilters(HttpServletRequest request) {
        for (SecurityFilterChain securityFilterChain : filterChains) {
            if (securityFilterChain.matches(request)) {
                return securityFilterChain.getFilters();
            }
        }
        return null;
    }

    private static final class VirtualFilterChain implements FilterChain {
        private final FilterChain originalChain;
        private final List<Filter> additionalFilters;
        private final int size;
        private int currentPosition = 0;
        private VirtualFilterChain(FilterChain chain, List<Filter> additionalFilters) {
            this.originalChain = chain;
            this.additionalFilters = additionalFilters;
            this.size = additionalFilters.size();
        }
        @Override
        public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
            if (this.currentPosition == this.size) {
                this.originalChain.doFilter(request, response);
                return;
            }
            this.currentPosition++;
            Filter nextFilter = this.additionalFilters.get(this.currentPosition - 1);
            nextFilter.doFilter(request, response, this);
        }
    }
}
