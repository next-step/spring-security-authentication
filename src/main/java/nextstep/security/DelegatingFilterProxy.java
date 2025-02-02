package nextstep.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class DelegatingFilterProxy extends GenericFilterBean {
    private final FilterChainProxy filterChainProxy;

    public DelegatingFilterProxy(final FilterChainProxy filterChainProxy) {
        this.filterChainProxy = filterChainProxy;
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        filterChainProxy.doFilter(request, response, chain);
    }
}
