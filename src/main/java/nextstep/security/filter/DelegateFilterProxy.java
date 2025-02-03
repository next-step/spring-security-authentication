package nextstep.security.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class DelegateFilterProxy extends GenericFilterBean {

    private final Filter delegate;

    public DelegateFilterProxy(Filter delegate) {
        this.delegate = delegate;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        delegate.doFilter(servletRequest, servletResponse, filterChain);
    }
}
