package nextstep.security.config;
import org.springframework.web.filter.GenericFilterBean;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
public class FilterChainProxy extends GenericFilterBean {
    private final List<SecurityFilterChain> filterChains;

    public FilterChainProxy(List<SecurityFilterChain> filterChains) {
        System.out.println("FilterChainProxy.FilterChainProxy");
        this.filterChains = filterChains;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("FilterChainProxy.doFilter");
        List<Filter> filters = getFilters((HttpServletRequest) request);
        VirtualFilterChain virtualFilterChain = new VirtualFilterChain(chain, filters);
        virtualFilterChain.doFilter(request, response);
    }

    private List<Filter> getFilters(HttpServletRequest request) {
        System.out.println("FilterChainProxy.getFilters");
        System.out.println("request = " + request);
        for (SecurityFilterChain chain : this.filterChains) {
            System.out.println("chain = " + chain); //DefaultSecurityFilterChain
            if (chain.matches(request)) {
                System.out.println("chain.getFilters() = " + chain.getFilters());
                return chain.getFilters();
            }
        }
        return null;
    }

    private static final class VirtualFilterChain implements FilterChain {

        private final FilterChain originalChain;
        private final List<Filter> additionalFilters;
        private final int size;
        private int currentPosition = 0;

        private VirtualFilterChain(FilterChain chain, List<Filter> additionalFilters) {
            System.out.println("VirtualFilterChain.VirtualFilterChain");
            for (Filter additionalFilter : additionalFilters) {
                System.out.println("additionalFilter = " + additionalFilter);
            }
            this.originalChain = chain;
            this.additionalFilters = additionalFilters;
            this.size = additionalFilters.size();
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
            System.out.println("VirtualFilterChain.doFilter");
            if (this.currentPosition == this.size) {
                System.out.println("currentPosition = " + currentPosition);
                System.out.println("size = " + size);
                System.out.println("originalChain = " + originalChain);
                this.originalChain.doFilter(request, response);
                return;
            }
            this.currentPosition++;
            Filter nextFilter = this.additionalFilters.get(this.currentPosition - 1);
            System.out.println("nextFilter = " + nextFilter);
            nextFilter.doFilter(request, response, this);
        }
    }
}