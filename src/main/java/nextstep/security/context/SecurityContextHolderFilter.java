package nextstep.security.context;

import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class SecurityContextHolderFilter extends GenericFilterBean {

    private final HttpSessionSecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("SecurityContextHolderFilter.doFilter");
        SecurityContext context = securityContextRepository.loadContext((HttpServletRequest) servletRequest);
        SecurityContextHolder.setContext(context);

        filterChain.doFilter(servletRequest, servletResponse);

        SecurityContextHolder.clearContext();
    }
}
