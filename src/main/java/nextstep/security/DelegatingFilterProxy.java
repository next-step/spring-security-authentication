package nextstep.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class DelegatingFilterProxy extends GenericFilterBean {
    private final FilterChainProxy delegate;

    public DelegatingFilterProxy(final FilterChainProxy delegate) {
        this.delegate = delegate;
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        delegate.invoke(request, response, chain);
    }
}
