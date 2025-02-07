package nextstep.security.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class FilterChainProxy extends GenericFilterBean {
    private final List<SecurityFilterChain> securityFilterChains;

    public FilterChainProxy(final List<SecurityFilterChain> securityFilterChains) {
        this.securityFilterChains = securityFilterChains;
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        List<Filter> filters = getFilters((HttpServletRequest) request);
        var virtualFilterChain = new VirtualFilterChain(filters, chain);
        virtualFilterChain.doFilter(request, response);
    }

    private List<Filter> getFilters(HttpServletRequest request) {
        return securityFilterChains.stream()
                .filter(it -> it.matches(request))
                .findFirst()
                .map(SecurityFilterChain::getFilters)
                .orElse(Collections.emptyList());
    }

}
