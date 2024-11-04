package nextstep.security.config;

import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public class FilterChainProxy extends GenericFilterBean {
    private final List<SecurityFilterChain> filterChains;

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
        for (SecurityFilterChain filterChain : filterChains) {
            if (filterChain.matches(request)) {
                return filterChain.getFilters();
            }
        }

        return null;
    }

    private static final class VirtualFilterChain implements FilterChain {

        private final FilterChain originalChain;
        private final List<Filter> additionalFilters;

        private final int size;
        private int currentPosition = 0;

        public VirtualFilterChain(FilterChain originalChain, List<Filter> additionalFilters) {
            this.originalChain = originalChain;
            this.additionalFilters = additionalFilters;
            this.size = additionalFilters.size();
        }

        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException, ServletException {
            if (currentPosition == size) {
                originalChain.doFilter(servletRequest, servletResponse);
                return;
            }
            this.currentPosition++;
            Filter nextFilter = additionalFilters.get(currentPosition - 1);
            nextFilter.doFilter(servletRequest, servletResponse, this);
        }
    }
}
