package nextstep.security.filter;

import nextstep.security.SecurityFilterChain;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class FilterChainProxy extends GenericFilterBean {

    private final List<SecurityFilterChain> filterChains;
    private final FilterChainDecorator filterChainDecorator;

    public FilterChainProxy(List<SecurityFilterChain> filterChains) {
        this.filterChains = filterChains;
        this.filterChainDecorator = new VirtualFilterChainDecorator();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doFilterInternal(request, response, chain);
    }

    private void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        // 1. 요청에 맞는 체인 안에 필터 조회
        List<Filter> filters = getFilters((HttpServletRequest) request);

        // 2. 필터 실행
        filterChainDecorator.decorate(chain, filters)
                .doFilter(request, response);
    }

    private List<Filter> getFilters(HttpServletRequest request) {
        return filterChains.stream()
                .filter(v -> v.matches(request))
                .map(SecurityFilterChain::getFilters)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public interface FilterChainDecorator {
        default FilterChain decorate(FilterChain original) {
            return this.decorate(original, List.of());
        }

        FilterChain decorate(FilterChain original, List<Filter> filters);
    }

    public static final class VirtualFilterChainDecorator implements FilterChainDecorator {
        public VirtualFilterChainDecorator() {
        }

        public FilterChain decorate(FilterChain original) {
            return original;
        }

        public FilterChain decorate(FilterChain original, List<Filter> filters) {
            return new VirtualFilterChain(original, filters);
        }
    }

    private static final class VirtualFilterChain implements FilterChain {
        private final FilterChain originalChain;
        private final List<Filter> additionalFilters;
        private final int size;
        private int currentPosition = 0;

        private VirtualFilterChain(FilterChain chain, List<Filter> additionalFilters) {
            this.originalChain = chain;
            this.additionalFilters = additionalFilters;
            this.size = additionalFilters.size();
        }

        public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
            if (this.currentPosition == this.size) {
                this.originalChain.doFilter(request, response);
            } else {
                // 현재 포지션 위치 찾는 거 였음..
                ++this.currentPosition;
                Filter nextFilter = this.additionalFilters.get(this.currentPosition - 1);
                nextFilter.doFilter(request, response, this);
            }
        }
    }
}
