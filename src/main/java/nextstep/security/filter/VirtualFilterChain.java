package nextstep.security.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import java.io.IOException;
import java.util.List;

public class VirtualFilterChain implements FilterChain {

    private final FilterChain originalChain;

    private final List<Filter> additionalFilters;

    private int currentPosition = 0;

    public VirtualFilterChain(FilterChain originalChain, List<Filter> additionalFilters) {
        this.originalChain = originalChain;
        this.additionalFilters = additionalFilters;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
        if (currentPosition == additionalFilters.size()) {
            originalChain.doFilter(request, response);
            return;
        }

        Filter nextFilter = additionalFilters.get(currentPosition++);
        nextFilter.doFilter(request, response, this);
    }
}
