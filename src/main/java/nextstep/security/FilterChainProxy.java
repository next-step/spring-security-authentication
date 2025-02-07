package nextstep.security;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.List;

public class FilterChainProxy extends GenericFilterBean {

    private final SecurityFilterChain securityFilterChain;

    public FilterChainProxy(SecurityFilterChain securityFilterChain) {
        this.securityFilterChain = securityFilterChain;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        List<Filter> filters = securityFilterChain.getFilters();

        var virtualFilterChain = new VirtualFilterChain(filters, filterChain);
        virtualFilterChain.doFilter(request, response);
    }
}
