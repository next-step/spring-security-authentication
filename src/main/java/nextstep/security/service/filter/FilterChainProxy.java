package nextstep.security.service.filter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;

@RequiredArgsConstructor
public class FilterChainProxy extends GenericFilterBean {

    private final List<SecurityFilterChain> securityFilterChains;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        List<Filter> filters = getFilters((HttpServletRequest) servletRequest);

        VirtualFilterChain virtualFilterChain = new VirtualFilterChain(filterChain, filters);
        virtualFilterChain.doFilter(servletRequest, servletResponse);
    }

    private List<Filter> getFilters(HttpServletRequest httpServletRequest) {
        for (SecurityFilterChain securityFilterChain : securityFilterChains) {
            if (securityFilterChain.matches(httpServletRequest)) {
                return securityFilterChain.getFilters();
            }
        }
        return Collections.emptyList();
    }

    private static final class VirtualFilterChain implements FilterChain {

        private final FilterChain originalChain;

        private final List<Filter> inputFilters;

        private final int inputFilterSize;

        private int currentPosition = 0;

        private VirtualFilterChain(FilterChain originalChain, List<Filter> inputFilters) {
            this.originalChain = originalChain;
            this.inputFilters = inputFilters;
            inputFilterSize = inputFilters.size();
        }

        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse)
                throws IOException, ServletException {

            if (currentPosition == inputFilterSize) {
                originalChain.doFilter(servletRequest, servletResponse);
                return;
            }

            currentPosition++;
            Filter nextFilter = inputFilters.get(currentPosition - 1);
            nextFilter.doFilter(servletRequest, servletResponse, this);
        }
    }
}
