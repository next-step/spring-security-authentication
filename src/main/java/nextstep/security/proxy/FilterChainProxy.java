package nextstep.security.proxy;

import jakarta.servlet.*;
import nextstep.security.filterchain.SecurityFilterChain;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class FilterChainProxy extends GenericFilterBean {
    private final List<SecurityFilterChain> filterChains;

    public FilterChainProxy(List<SecurityFilterChain> filters) {
        this.filterChains = filters;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain originalChain) throws IOException, ServletException {

        SecurityFilterChain matchedChain = filterChains.stream()
                .filter(chain -> chain.matches(servletRequest))
                .findFirst()
                .orElse(null);

        if (Objects.nonNull(matchedChain)) {
            List<Filter> filters = matchedChain.getFilters();
            VirtualFilterChain virtualFilterChain = new VirtualFilterChain(filters, originalChain);
            virtualFilterChain.doFilter(servletRequest, servletResponse);
        }

    }


    public class VirtualFilterChain implements FilterChain {
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
                Filter filter = filters.get(currentPosition++);
                filter.doFilter(request, response, this);
            }
        }
    }
}
