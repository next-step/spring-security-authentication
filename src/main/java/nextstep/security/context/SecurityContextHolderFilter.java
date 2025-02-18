package nextstep.security.context;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class SecurityContextHolderFilter extends GenericFilterBean {

    private final HttpSessionSecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain) throws IOException, ServletException {
        try {
            final SecurityContext context = this.securityContextRepository.loadContext((HttpServletRequest) request);
            SecurityContextHolder.setContext(context);

            filterChain.doFilter(request, response);

            securityContextRepository.saveContext(SecurityContextHolder.getContext(), (HttpServletRequest) request, (HttpServletResponse) response);
        } finally {
            SecurityContextHolder.clearContext();
        }

    }
}
