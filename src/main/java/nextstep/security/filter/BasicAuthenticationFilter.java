package nextstep.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.Authentication;
import nextstep.security.AuthenticationConverter;
import nextstep.security.AuthenticationEntrypoint;
import nextstep.security.UserDetails;
import nextstep.security.UserDetailsService;
import nextstep.security.exception.AuthenticationException;
import nextstep.security.exception.BadCredentialsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class BasicAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(BasicAuthenticationFilter.class);

    private final UserDetailsService userDetailsService;
    private final AuthenticationConverter authenticationConverter;
    private final AuthenticationEntrypoint authenticationEntrypoint;

    public BasicAuthenticationFilter(UserDetailsService userDetailsService,
                                     AuthenticationConverter authenticationConverter,
                                     AuthenticationEntrypoint authenticationEntrypoint) {

        this.userDetailsService = userDetailsService;
        this.authenticationConverter = authenticationConverter;
        this.authenticationEntrypoint = authenticationEntrypoint;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (!request.getRequestURI().equals("/members")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Authentication authRequest = this.authenticationConverter.convert(request);

            if (authRequest == null) {
                filterChain.doFilter(request, response);
                return;
            }

            String principal = authRequest.getPrincipal().toString();
            String credentials = authRequest.getCredentials().toString();
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal);

            if (!userDetails.getPassword().equals(credentials)) {
                throw new BadCredentialsException("Bad Credentials %s".formatted(principal));
            }

        } catch (AuthenticationException e) {
            logger.error("Basic Authentication error", e);
            authenticationEntrypoint.commence(request, response, e);
        }

        filterChain.doFilter(request, response);

    }
}
