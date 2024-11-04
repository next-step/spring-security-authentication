package nextstep.security.filter;

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

    // 필터가 아닌 필터 체인을 목록으로 가지는 이유?
    private final List<SecurityFilterChain> filterChains;

    public FilterChainProxy(List<SecurityFilterChain> filterChains) {
        this.filterChains = filterChains;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
            FilterChain filterChain) throws IOException, ServletException {
        // 필터 체인에서 일치하는 필터 목록을 가져온다.
        List<Filter> filters = getFilterChain(servletRequest);

        // 필터 체인과 찾은 목록으로 버츄얼 필터체인을 생성한다.
        VirtualFilterChain virtualFilterChain = new VirtualFilterChain(filterChain, filters);

        // 생성한 버츄얼 필터 체인이 실행된다.
        virtualFilterChain.doFilter(servletRequest, servletResponse);
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
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse)
                throws IOException, ServletException {

            // 인덱스와 필터 목록의 갯수를 비교한다.
            if (this.currentPosition == this.size) {
                this.originalChain.doFilter(servletRequest, servletResponse);
                return;
            }

            // 인덱스를 한칸 이동
            this.currentPosition++;

            // 실행할 필터를 선택
            Filter nextFilter = additionalFilters.get(currentPosition - 1);

            // 필터를 실행한다.
            nextFilter.doFilter(servletRequest, servletResponse, this);
        }
    }

    private List<Filter> getFilterChain(ServletRequest servletRequest) {
        for (SecurityFilterChain chain : filterChains) {
            if (chain.matches((HttpServletRequest) servletRequest)) {
                return chain.getFilters();
            }
        }
        return null;
    }

}
