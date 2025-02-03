package nextstep.security;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.exception.AuthenticationException;

import java.io.IOException;
import java.util.List;

public class FilterChainProxy {
    private final List<SecurityFilterChain> securityFilterChains;

    public FilterChainProxy(final List<SecurityFilterChain> securityFilterChains) {
        this.securityFilterChains = securityFilterChains;
    }


    public void invoke(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws ServletException, IOException {
        SecurityFilterChain filterChain = findFilterChain(request);

        VirtualFilterChain virtualFilterChain = new VirtualFilterChain(filterChain.filters(), chain);
        virtualFilterChain.doFilter(request, response);
    }

    private SecurityFilterChain findFilterChain(final ServletRequest request) {
        for (SecurityFilterChain securityFilterChain: securityFilterChains) {
            if (securityFilterChain.match((HttpServletRequest) request)) {
                return securityFilterChain;
            }
        }

        throw new AuthenticationException();
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
            if (currentPosition == filters.size()) {
                originalChain.doFilter(request, response);
            } else {
                Filter nextFilter = filters.get(currentPosition++);
                nextFilter.doFilter(request, response, this);
            }
        }
    }
}
