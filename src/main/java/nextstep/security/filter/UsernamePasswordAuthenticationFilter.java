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
import nextstep.security.exception.UsernameNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class UsernamePasswordAuthenticationFilter extends GenericFilterBean {
    private static final Logger logger = LoggerFactory.getLogger(UsernamePasswordAuthenticationFilter.class);
    private static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";
    private static final String DEFAULT_FILTER_PROCESS_URL = "/login";

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
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        if (!requireAuthentication(httpRequest)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Authentication authRequest = this.authenticationConverter.convert(httpRequest);

            if (authRequest == null) {
                filterChain.doFilter(request, response);
                return;
            }

            String username = obtainUsername(authRequest);
            String password = obtainPassword(authRequest);

            UserDetails user = tryRetrieveUser(username);
            checkPassword(user, password);

            HttpSession session = httpRequest.getSession();
            session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, user);
        } catch (AuthenticationException e) {
            logger.error("UsernamePassword Authentication failed", e);
            authenticationEntrypoint.commence((HttpServletRequest) request, (HttpServletResponse) response, e);
        }
    }

    private boolean requireAuthentication(HttpServletRequest httpRequest) {
        return httpRequest.getRequestURI().equals(DEFAULT_FILTER_PROCESS_URL);
    }

    private void checkPassword(UserDetails user, String password) {
        boolean passwordValid = user.getPassword().equals(password);
        if (!passwordValid) {
            throw new BadCredentialsException("Bad Credentials");
        }
    }

    private UserDetails tryRetrieveUser(String principal) {
        try {
            return userDetailsService.loadUserByUsername(principal);
        } catch (Exception e) {
            logger.error("Fail to get Authentication user", e);
            throw new UsernameNotFoundException("Fail to get Authentication user", e);
        }
    }

    private String obtainUsername(Authentication authRequest) {
        return authRequest.getPrincipal().toString();
    }

    private String obtainPassword(Authentication authRequest) {
        return authRequest.getCredentials().toString();
    }

}
