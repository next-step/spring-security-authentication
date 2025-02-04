package nextstep.security.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import java.io.IOException;
import java.util.List;

public final class VirtualFilterChain implements FilterChain {
    private final FilterChain originalChain;
    private final List<Filter> additionalFilters;
    private int currentPosition = 0;

    public VirtualFilterChain(FilterChain originalChain, List<Filter> additionalFilters) {
        this.originalChain = originalChain;
        this.additionalFilters = additionalFilters;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
        if (hasNextFilter()) {
            nextFilter().doFilter(request, response, this);
        } else {
            this.originalChain.doFilter(request, response);
        }
    }

    private boolean hasNextFilter() {
        return this.currentPosition < this.additionalFilters.size();
    }

    private Filter nextFilter() {
        return this.additionalFilters.get(this.currentPosition++);
    }
}
