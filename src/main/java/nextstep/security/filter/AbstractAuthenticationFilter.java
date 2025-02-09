package nextstep.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.context.SecurityContextHolder;
import nextstep.security.authentication.context.SecurityContextImpl;
import nextstep.security.authentication.context.SecurityContextRepository;
import nextstep.security.authentication.context.SessionSecurityContextRepository;
import nextstep.security.authentication.provider.AuthenticationManager;
import nextstep.security.exception.AuthenticationException;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public abstract class AbstractAuthenticationFilter extends GenericFilterBean {

    protected final AuthenticationManager authenticationManager;
    protected final SecurityContextRepository securityContextRepository;

    public AbstractAuthenticationFilter(final AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = new SessionSecurityContextRepository();
    }

    public abstract Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;

    public abstract boolean support(HttpServletRequest request);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            Authentication authentication = attemptAuthentication((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            securityContextRepository.saveContext(SecurityContextImpl.from(authentication), (HttpServletRequest) servletRequest);

            filterChain.doFilter(servletRequest, servletResponse);
        } catch (AuthenticationException e) {
            ((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }
}
