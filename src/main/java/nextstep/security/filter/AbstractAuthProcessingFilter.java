package nextstep.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.core.context.SecurityContext;
import nextstep.security.core.context.SecurityContextHolder;
import nextstep.security.core.context.SecurityContextRepository;
import nextstep.security.exception.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public abstract class AbstractAuthProcessingFilter extends OncePerRequestFilter {
    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    protected AbstractAuthProcessingFilter(final AuthenticationManager authenticationManager, final SecurityContextRepository securityContextRepository) {
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        if (!match(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Authentication authRequest = makeAuthentication(request);

            Authentication authentication = authenticate(authRequest);

            saveAuthentication(request, response, authentication);

            successAuthentication(request, response, filterChain);
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
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

        securityContextRepository.saveContext(securityContext, request, response);
    }

    abstract Authentication makeAuthentication(final HttpServletRequest request);

    protected abstract void successAuthentication(final HttpServletRequest request, final HttpServletResponse response,
                                                  final FilterChain filterChain) throws ServletException, IOException;
}
