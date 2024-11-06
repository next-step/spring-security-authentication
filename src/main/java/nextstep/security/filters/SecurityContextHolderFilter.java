package nextstep.security.filters;

import nextstep.security.context.HttpSessionSecurityContextRepository;
import nextstep.security.context.SecurityContext;
import nextstep.security.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class SecurityContextHolderFilter extends GenericFilterBean {

    private final HttpSessionSecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        SecurityContext context = this.securityContextRepository.loadContext((HttpServletRequest) servletRequest);
        SecurityContextHolder.setContext(context);

        filterChain.doFilter(servletRequest, servletResponse);

        SecurityContextHolder.clearContext();


    }
}
