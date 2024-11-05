package nextstep.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.filter.GenericFilterBean;

public class SecurityContextHolderFilter extends GenericFilterBean {

    private final HttpSessionSecurityContextRepository sessionSecurityContextRepository = new HttpSessionSecurityContextRepository();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        SecurityContext context = this.sessionSecurityContextRepository.loadContext((HttpServletRequest) request);
        SecurityContextHolder.setContext(context);

        chain.doFilter(request, response);

        SecurityContextHolder.clearContext();
    }
}
