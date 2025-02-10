package nextstep.authentication.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.authentication.context.SecurityContextHolder;
import nextstep.authentication.context.SecurityContextRepository;

import java.io.IOException;

public class SecurityContextHolderFilter implements Filter {

    private final SecurityContextRepository securityContextRepository;

    public SecurityContextHolderFilter(SecurityContextRepository securityContextRepository) {
        this.securityContextRepository = securityContextRepository;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        var securityContext = securityContextRepository.loadContext(request);
        SecurityContextHolder.setContext(securityContext);

        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            securityContextRepository.saveContext(SecurityContextHolder.getContext(), request, response);
            SecurityContextHolder.clearContext();
        }

    }
}
