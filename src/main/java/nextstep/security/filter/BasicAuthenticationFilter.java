package nextstep.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
        try {
            Authentication authRequest = this.authenticationConverter.convert(request);

            if (authRequest == null) {
                filterChain.doFilter(request, response);
                return;
            }

            String username = obtainUsername(authRequest);
            String password = obtainPassword(authRequest);

            UserDetails user = tryRetrieveUser(username);
            checkPassword(user, password);
        } catch (AuthenticationException e) {
            logger.error("Basic Authentication failed", e);
            authenticationEntrypoint.commence(request, response, e);
        }

        filterChain.doFilter(request, response);

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
