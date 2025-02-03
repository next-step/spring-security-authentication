package nextstep.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.Authentication;
import nextstep.security.AuthenticationConverter;
import nextstep.security.AuthenticationEntrypoint;
import nextstep.security.AuthenticationManager;
import nextstep.security.SecurityContext;
import nextstep.security.UserDetails;
import nextstep.security.UserDetailsService;
import nextstep.security.context.SecurityContextHolder;
import nextstep.security.exception.AuthenticationException;
import nextstep.security.exception.BadCredentialsException;
import nextstep.security.exception.UsernameNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class BasicAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(BasicAuthenticationFilter.class);

    private final AuthenticationManager authenticationManager;
    private final AuthenticationConverter authenticationConverter;
    private final AuthenticationEntrypoint authenticationEntrypoint;

    public BasicAuthenticationFilter(AuthenticationManager authenticationManager,
                                     AuthenticationConverter authenticationConverter,
                                     AuthenticationEntrypoint authenticationEntrypoint) {

        this.authenticationManager = authenticationManager;
        this.authenticationConverter = authenticationConverter;
        this.authenticationEntrypoint = authenticationEntrypoint;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            Authentication authRequest = this.authenticationConverter.convert(request);

            if (authRequest == null) {
                filterChain.doFilter(request, response);
                return;
            }

            Authentication authResult = this.authenticationManager.authenticate(authRequest);
            setSecurityContext(authResult);

        } catch (AuthenticationException e) {
            logger.error("Basic Authentication failed", e);
            authenticationEntrypoint.commence(request, response, e);
            SecurityContextHolder.clearContext();
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    private void setSecurityContext(Authentication authResult) {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authResult);
        SecurityContextHolder.setContext(securityContext);
    }
}
