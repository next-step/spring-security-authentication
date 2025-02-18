package nextstep.security;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.List;

public class FilterChainProxy extends GenericFilterBean {

    private final List<SecurityFilterChain> filterChains;

    public FilterChainProxy(final List<SecurityFilterChain> securityFilterChains) {
        this.filterChains = securityFilterChains;
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        final List<Filter> filters = getFilters((HttpServletRequest) request);

        final VirtualFilterChain virtualFilterChain = new VirtualFilterChain(filters, chain);
        virtualFilterChain.doFilter(request, response);
    }

    private List<Filter> getFilters(final HttpServletRequest request) {
        for (SecurityFilterChain chain : this.filterChains) {
            if (chain.matches(request)) {
                return chain.getFilters();
            }
        }

        return null;
    }

    public class VirtualFilterChain implements FilterChain {
        private final List<Filter> filters;
        private final FilterChain originalChain;
        private final int size;
        private int currentPosition = 0;

        public VirtualFilterChain(final List<Filter> filters, final FilterChain originalChain) {
            this.filters = filters;
            this.originalChain = originalChain;
            this.size = filters.size();
        }

        @Override
        public void doFilter(final ServletRequest request, final ServletResponse response) throws IOException, ServletException {
            if (this.currentPosition == this.size) {
                this.originalChain.doFilter(request, response);
                return;
            }

            final Filter nextFilter = filters.get(currentPosition++);
            nextFilter.doFilter(request, response, this);
        }
    }
}
