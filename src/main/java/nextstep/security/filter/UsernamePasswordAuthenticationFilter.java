package nextstep.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.security.Authentication;
import nextstep.security.AuthenticationConverter;
import nextstep.security.AuthenticationEntrypoint;
import nextstep.security.UserDetails;
import nextstep.security.UserDetailsService;
import nextstep.security.exception.AuthenticationException;
import nextstep.security.exception.BadCredentialsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class UsernamePasswordAuthenticationFilter extends GenericFilterBean {
    private static final Logger logger = LoggerFactory.getLogger(UsernamePasswordAuthenticationFilter.class);
    private static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";

    private final UserDetailsService userDetailsService;
    private final AuthenticationConverter authenticationConverter;
    private final AuthenticationEntrypoint authenticationEntrypoint;

    public UsernamePasswordAuthenticationFilter(UserDetailsService userDetailsService,
                                                AuthenticationConverter authenticationConverter,
                                                AuthenticationEntrypoint authenticationEntrypoint) {

        this.userDetailsService = userDetailsService;
        this.authenticationConverter = authenticationConverter;
        this.authenticationEntrypoint = authenticationEntrypoint;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
            Authentication authRequest = this.authenticationConverter.convert(httpRequest);

            if (authRequest == null) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }

            String principal = authRequest.getPrincipal().toString();
            String credentials = authRequest.getCredentials().toString();
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal);

            if (!userDetails.getPassword().equals(credentials)) {
                throw new BadCredentialsException("Bad Credentials %s".formatted(principal));
            }

            HttpSession session = httpRequest.getSession();
            session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, userDetails);

        } catch (AuthenticationException e) {
            logger.error("Authentication error", e);
            authenticationEntrypoint.commence((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, e);
        } catch (Exception e) {
            logger.error("Authentication error", e);
            ((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
