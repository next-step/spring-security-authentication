package nextstep.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class FilterChainProxy extends GenericFilterBean {

    private final SecurityFilterChain securityFilterChain;

    public FilterChainProxy(SecurityFilterChain securityFilterChain) {
        this.securityFilterChain = securityFilterChain;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

    }
}
