package nextstep.security.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.List;

public class FilterChainProxy extends GenericFilterBean {

    private final List<SecurityFilterChain> filterChains;

    private VirtualFilterChainDecorator virtualFilterChainDecorator = new VirtualFilterChainDecorator();

    public FilterChainProxy(List<SecurityFilterChain> filterChains) {
        this.filterChains = filterChains;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        List<Filter> filters = getFilters(request);
        
        if (filters == null || filters.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }
        this.virtualFilterChainDecorator.decorate(filterChain, filters).doFilter(request, response);
    }

    private List<Filter> getFilters(HttpServletRequest request) {
        return this.filterChains.stream()
                .filter(chain -> chain.matches(request))
                .findAny()
                .map(SecurityFilterChain::getFilters)
                .orElse(null);
    }

    public static final class VirtualFilterChainDecorator {

        public FilterChain decorate(FilterChain original, List<Filter> filters) {
            return new VirtualFilterChain(original, filters);
        }

    }

    private static class VirtualFilterChain implements FilterChain {

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
