package nextstep.security.filter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.filter.GenericFilterBean;

public class FilterChainProxy extends GenericFilterBean {

    List<SecurityFilterChain> securityFilterChainList;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        List<Filter> filters = getFilters((HttpServletRequest) request);

        VirtualFilterChain virtualFilterChain = new VirtualFilterChain(chain, filters);
        virtualFilterChain.doFilter(request, response);
    }

    private List<Filter> getFilters(HttpServletRequest request) {
        return securityFilterChainList.stream()
                .filter(it -> it.matches(request))
                .flatMap(it -> it.getFilters().stream()).collect(Collectors.toList());
    }

    private static final class VirtualFilterChain implements FilterChain {
        private final FilterChain originalChain;
        private final List<Filter> additionalFilters;
        private final int size;
        private int currentPosition = 0;

        private VirtualFilterChain(FilterChain originalChain, List<Filter> additionalFilters) {
            this.originalChain = originalChain;
            this.additionalFilters = additionalFilters;
            this.size = additionalFilters.size();
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response)
                throws IOException, ServletException {
            if (this.currentPosition == this.size) {
                originalChain.doFilter(request, response);
                return;
            }

            this.currentPosition++;
            Filter nextFilter = this.additionalFilters.get(this.currentPosition - 1);
            nextFilter.doFilter(request, response, this);
        }
    }
}
