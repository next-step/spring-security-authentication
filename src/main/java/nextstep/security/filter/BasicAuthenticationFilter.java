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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class BasicAuthenticationFilter extends GenericFilterBean {
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
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        try {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            Authentication authRequest = this.authenticationConverter.convert(httpRequest);

            if (authRequest == null) {
                filterChain.doFilter(request, response);
                return;
            }

            String principal = authRequest.getPrincipal().toString();
            String credentials = authRequest.getCredentials().toString();
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal);

            if (!userDetails.getPassword().equals(credentials)) {
                return;
            }

        } catch (AuthenticationException e) {
            logger.error("Authentication error", e);
            authenticationEntrypoint.commence((HttpServletRequest) request, (HttpServletResponse) response, e);
        }

        filterChain.doFilter(request, response);
    }
}
