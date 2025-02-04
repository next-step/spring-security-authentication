package nextstep.security.filter;

import jakarta.servlet.*;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.List;

public class FilterChainProxy extends GenericFilterBean {

    private final List<SecurityFilterChain> filterChains;

    public FilterChainProxy(List<SecurityFilterChain> filterChains) {
        this.filterChains = filterChains;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        for (SecurityFilterChain filterChain : filterChains) {
            List<Filter> filters = filterChain.filters();
            VirtualFilterChain virtualFilterChain = new VirtualFilterChain(filters, chain);
            virtualFilterChain.doFilter(servletRequest, servletResponse);
        }
    }

    public static class VirtualFilterChain implements FilterChain {

        private final List<Filter> filters;
        private final FilterChain originalChain;
        private int currentPosition = 0;

        public VirtualFilterChain(List<Filter> filters, FilterChain originalChain) {
            this.filters = filters;
            this.originalChain = originalChain;
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
            if (currentPosition >= filters.size()) {
                originalChain.doFilter(request, response);
            } else {
                Filter filter = filters.get(currentPosition++);
                filter.doFilter(request, response, this);
            }
        }
    }
}
