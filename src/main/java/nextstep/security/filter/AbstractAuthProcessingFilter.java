package nextstep.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.core.context.SecurityContext;
import nextstep.security.core.context.SecurityContextHolder;
import nextstep.security.exception.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public abstract class AbstractAuthProcessingFilter extends OncePerRequestFilter {
    private final AuthenticationManager authenticationManager;

    protected AbstractAuthProcessingFilter(final AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        if (!match(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (isAlreadyAuthenticated()) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authRequest = makeAuthentication(request);

        Authentication authentication = authenticate(authRequest);

        saveAuthentication(request, response, authentication);

        successAuthentication(request, response, filterChain);

        if (!shouldContinueFilterChain()) {
            return;
        }

        doFilter(request, response, filterChain);
    }

    private static boolean isAlreadyAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() != null;
    }

    private Authentication authenticate(final Authentication authRequest) {
        Authentication authentication = this.authenticationManager.authenticate(authRequest);
        if (!authentication.isAuthenticated()) {
            throw new AuthenticationException();
        }

        return authentication;
    }

    abstract boolean match(final HttpServletRequest request);

    private void saveAuthentication(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    abstract Authentication makeAuthentication(final HttpServletRequest request);

    protected abstract void successAuthentication(final HttpServletRequest request, final HttpServletResponse response,
                                                  final FilterChain filterChain) throws ServletException, IOException;

    protected boolean shouldContinueFilterChain() {
        return true;
    }
}
