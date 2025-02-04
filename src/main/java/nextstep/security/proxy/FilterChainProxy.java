package nextstep.security.proxy;

import jakarta.servlet.*;
import nextstep.security.filterchain.SecurityFilterChain;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.List;

public class FilterChainProxy extends GenericFilterBean {
    private final List<SecurityFilterChain> fiterChains;

    public FilterChainProxy(List<SecurityFilterChain> fiters) {
        this.fiterChains = fiters;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain originalChain) throws IOException, ServletException {

        List<SecurityFilterChain> supportedChain = fiterChains.stream()
                .filter(chain -> chain.supports(servletRequest))
                .toList();

        for (SecurityFilterChain chain : supportedChain) {
            List<Filter> filters = chain.getFilters();
            VirtualFilterChain virtualFilterChain = new VirtualFilterChain(filters, originalChain);
            virtualFilterChain.doFilter(servletRequest, servletResponse);
        }
    }

    public class VirtualFilterChain implements FilterChain {
        private final List<Filter> fiters;
        private final FilterChain originalChain;
        private int currentPosition = 0;

        public VirtualFilterChain(List<Filter> fiters, FilterChain originalChain) {
            this.fiters = fiters;
            this.originalChain = originalChain;
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
            if (currentPosition == fiters.size()) {
                originalChain.doFilter(request, response);
            } else {
                Filter filter = fiters.get(currentPosition++);
                filter.doFilter(request, response, this);
            }
        }
    }
}
