package nextstep.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.SecurityContext;
import nextstep.security.SecurityContextHolder;
import nextstep.security.SecurityContextRepository;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class SecurityContextHolderFilter extends GenericFilterBean {

    private final SecurityContextRepository securityContextRepository;

    public SecurityContextHolderFilter(SecurityContextRepository securityContextRepository) {
        this.securityContextRepository = securityContextRepository;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        SecurityContext securityContext = securityContextRepository.loadContext(httpRequest);
        SecurityContextHolder.setContext(securityContext);

        try {
            chain.doFilter(httpRequest, response);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }
}
