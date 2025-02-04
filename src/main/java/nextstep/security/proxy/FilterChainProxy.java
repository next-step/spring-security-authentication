package nextstep.security.proxy;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import nextstep.security.filterchain.SecurityFilterChain;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.List;

public class FilterChainProxy extends GenericFilterBean {
    private final List<SecurityFilterChain> fiterChains;
    private final FilterChain originalChain;

    public FilterChainProxy(List<SecurityFilterChain> fiters, FilterChain originalChain) {
        this.fiterChains = fiters;
        this.originalChain = originalChain;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    }
}
