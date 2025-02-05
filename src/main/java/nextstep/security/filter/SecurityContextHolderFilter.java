package nextstep.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
        SecurityContext securityContext = securityContextRepository.loadContext((HttpServletRequest) request);
        SecurityContextHolder.setContext(securityContext);

        chain.doFilter(request,response);

        SecurityContext context = SecurityContextHolder.getContext();
        securityContextRepository.saveContext(context, (HttpServletRequest) request, (HttpServletResponse) response);
        SecurityContextHolder.clearContext();
    }
}
