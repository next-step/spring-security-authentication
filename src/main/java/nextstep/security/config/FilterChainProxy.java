package nextstep.security.config;

import java.io.IOException;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.filter.GenericFilterBean;

public class FilterChainProxy extends GenericFilterBean {

    private final List<SecurityFilterChain> securityFilterChains;

    public FilterChainProxy(List<SecurityFilterChain> securityFilterChains) {
        this.securityFilterChains = securityFilterChains;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
            FilterChain filterChain) throws IOException, ServletException {
        var filters = getFilters(servletRequest);

        VirtualFilterChain virtualFilterChain = new VirtualFilterChain(filterChain, filters);
        virtualFilterChain.doFilter(servletRequest, servletResponse);
    }

    private List<Filter> getFilters(ServletRequest servletRequest) {
        var request = (HttpServletRequest) servletRequest;

        for (SecurityFilterChain chain : securityFilterChains) {
            if (chain.matches(request)) {
                return chain.getFilters();
            }
        }
        return null;
    }

    private static final class VirtualFilterChain implements FilterChain {

        private final FilterChain originFilterChain;

        private final List<Filter> additionalFilters;

        private final int size;

        private int currentPosition;

        private VirtualFilterChain(FilterChain originFilterChain, List<Filter> additionalFilters) {
            this.originFilterChain = originFilterChain;
            this.additionalFilters = additionalFilters;
            this.size = additionalFilters.size();
        }


        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse)
                throws IOException, ServletException {
            if (this.currentPosition == this.size) {
                this.originFilterChain.doFilter(servletRequest, servletResponse);
                return;
            }
            this.currentPosition++;

            var nexFilter = this.additionalFilters.get(this.currentPosition - 1);
            nexFilter.doFilter(servletRequest, servletResponse, this);

        }
    }

}
