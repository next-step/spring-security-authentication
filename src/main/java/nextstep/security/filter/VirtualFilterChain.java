package nextstep.security.filter;

import jakarta.servlet.*;

import java.io.IOException;
import java.util.List;

public class VirtualFilterChain implements FilterChain {

    private final List<Filter> filters;
    private int position = 0;

    private final FilterChain originalChain;

    public VirtualFilterChain(final List<Filter> filters, final FilterChain originalChain) {
        this.filters = filters;
        this.originalChain = originalChain;
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response) throws IOException, ServletException {
        if (filters.size() == position) {
            originalChain.doFilter(request, response);
            return;
        }
        var nextFilter = filters.get(position);
        position++;
        nextFilter.doFilter(request, response, this);
    }

}
