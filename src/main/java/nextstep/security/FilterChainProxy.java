package nextstep.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class FilterChainProxy implements Filter {

    private final List<SecurityFilterChain> securityFilterChains;

    public FilterChainProxy(List<SecurityFilterChain> securityFilterChains) {
        this.securityFilterChains = securityFilterChains;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Optional<SecurityFilterChain> optionalSecurityFilterChain = securityFilterChains.stream()
                .filter((it) -> it.matches((HttpServletRequest) request))
                .findFirst();

        if (optionalSecurityFilterChain.isPresent()) {
            VirtualFilterChain virtualFilterChain = new VirtualFilterChain(optionalSecurityFilterChain.get().getFilters(), chain);
            virtualFilterChain.doFilter(request, response);
        }
    }

    private static class VirtualFilterChain implements FilterChain {
        private final List<Filter> filters;
        private final FilterChain originalChain;
        private int currentPosition = 0;

        public VirtualFilterChain(List<Filter> filters, FilterChain originalChain) {
            this.filters = filters;
            this.originalChain = originalChain;
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
            if (currentPosition == filters.size()) {
                originalChain.doFilter(request, response);
            } else {
                Filter nextFilter = filters.get(currentPosition++);
                nextFilter.doFilter(request, response, this);
            }
        }
    }
}
