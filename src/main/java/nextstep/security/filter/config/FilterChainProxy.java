package nextstep.security.filter.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class FilterChainProxy extends GenericFilterBean {

    private final List<SecurityFilterChain> filterChains;

    public FilterChainProxy(List<SecurityFilterChain> filterChains) {
        this.filterChains = filterChains;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 현재 요청에 적용할 필터 그룹을 찾아온다
        List<Filter> filters = getFilters((HttpServletRequest) request);
        // 기존 필터 체인과 현재 요청에 맞는 필터들을 합쳐서 가상 체인을 만든다
        VirtualFilterChain virtualFilterChain = new VirtualFilterChain(chain, filters);
        virtualFilterChain.doFilter(request, response);
    }

    /**
     * 현재 HTTP 요청에 적용할 필터 목록을 찾아 반환합니다.
     */
    private List<Filter> getFilters(HttpServletRequest request) {
        // 일치하는 필터 그룹을 찾아서 던져준다
        for (SecurityFilterChain chain : this.filterChains) {
            if (chain.matches(request)) {
                return chain.getFilters();
            }
        }

        return Collections.emptyList();
    }

    /**
     * 원본 필터 체인과 추가 필터들을 관리하는 가상 필터 체인.
     * 커스텀 필터 그룹과 기존 필터 체인을 순차적으로 실행합니다.
     */
    private static final class VirtualFilterChain implements FilterChain {

        private final FilterChain originalChain;
        private final List<Filter> additionalFilters;
        private final int size;

        private int currentPosition = 0;

        /**
         * @param chain             기존 Tomcat 등에서 설정된 필터 체인
         * @param additionalFilters 현재 요청에 맞는 추가 필터 그룹
         */
        private VirtualFilterChain(FilterChain chain, List<Filter> additionalFilters) {
            this.originalChain = chain;
            this.additionalFilters = additionalFilters;
            this.size = additionalFilters.size();
        }

        /**
         * 필터들을 순차적으로 실행합니다.
         * 실행 순서: 커스텀 필터 그룹 -> 기존 필터 그룹 -> 비즈니스 로직 -> 기존 필터 그룹 -> 커스텀 필터 그룹
         */
        @Override
        public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
            if (this.currentPosition == this.size) {
                this.originalChain.doFilter(request, response);
                return;
            }
            this.currentPosition++;
            Filter nextFilter = this.additionalFilters.get(this.currentPosition - 1);
            nextFilter.doFilter(request, response, this);
        }
    }
}