package nextstep.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.context.SecurityContext;
import nextstep.security.context.SecurityContextHolder;
import nextstep.security.context.SecurityContextRepository;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class SecurityContextHolderFilter extends GenericFilterBean {
    private final SecurityContextRepository securityContextRepository;

    public SecurityContextHolderFilter(SecurityContextRepository securityContextRepository) {
        this.securityContextRepository = securityContextRepository;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            SecurityContext securityContext = securityContextRepository.loadContext((HttpServletRequest) request);
            SecurityContextHolder.setContext(securityContext);

            chain.doFilter(request, response);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }
}
