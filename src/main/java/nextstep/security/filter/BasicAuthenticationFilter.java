package nextstep.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationFailureHandler;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.AuthenticationSuccessHandler;
import nextstep.security.converter.AuthenticationConverter;
import nextstep.security.exception.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class BasicAuthenticationFilter extends OncePerRequestFilter {
    private final AuthenticationManager authenticationManager;
    private final AuthenticationConverter authenticationConverter;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;

    public BasicAuthenticationFilter(AuthenticationManager authenticationManager,
                                     AuthenticationConverter authenticationConverter,
                                     AuthenticationSuccessHandler authenticationSuccessHandler,
                                     AuthenticationFailureHandler authenticationFailureHandler) {

        this.authenticationManager = authenticationManager;
        this.authenticationConverter = authenticationConverter;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
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
            authenticationSuccessHandler.onAuthenticationSuccess(request, response, authResult);
        } catch (AuthenticationException e) {
            authenticationFailureHandler.onAuthenticationFailure(request, response, e);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
