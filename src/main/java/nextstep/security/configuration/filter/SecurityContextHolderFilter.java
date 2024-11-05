package nextstep.security.configuration.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import nextstep.security.model.context.SecurityContext;
import nextstep.security.repository.HttpSessionSecurityContextRepository;
import nextstep.security.service.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

public class SecurityContextHolderFilter extends GenericFilterBean {

    private final HttpSessionSecurityContextRepository httpSessionSecurityContextRepository = new HttpSessionSecurityContextRepository();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        SecurityContext context = httpSessionSecurityContextRepository.loadContext(
                (HttpServletRequest) servletRequest);

        SecurityContextHolder.setContext(context);
        filterChain.doFilter(servletRequest, servletResponse);
        SecurityContextHolder.clearContext();
    }
}
