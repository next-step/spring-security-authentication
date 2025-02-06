package nextstep.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationFailureHandler;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.AuthenticationSuccessHandler;
import nextstep.security.context.SecurityContext;
import nextstep.security.context.SecurityContextHolder;
import nextstep.security.context.SecurityContextHolderStrategy;
import nextstep.security.context.SecurityContextRepository;
import nextstep.security.converter.AuthenticationConverter;
import nextstep.security.exception.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class BasicAuthenticationFilter extends OncePerRequestFilter {
    private final SecurityContextHolderStrategy securityContextHolderStrategy =
            SecurityContextHolder.getContextHolderStrategy();

    private final AuthenticationManager authenticationManager;
    private final AuthenticationConverter authenticationConverter;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final SecurityContextRepository securityContextRepository;

    public BasicAuthenticationFilter(AuthenticationManager authenticationManager,
                                     AuthenticationConverter authenticationConverter,
                                     AuthenticationSuccessHandler authenticationSuccessHandler,
                                     AuthenticationFailureHandler authenticationFailureHandler,
                                     SecurityContextRepository securityContextRepository) {

        this.authenticationManager = authenticationManager;
        this.authenticationConverter = authenticationConverter;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.securityContextRepository = securityContextRepository;
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
            saveSecurityContext(authResult, request, response);
            authenticationSuccessHandler.onAuthenticationSuccess(request, response, authResult);
        } catch (AuthenticationException e) {
            authenticationFailureHandler.onAuthenticationFailure(request, response, e);
            return;
        }

        filterChain.doFilter(request, response);
    }


    private void saveSecurityContext(Authentication authResult, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        SecurityContext ctx = this.securityContextHolderStrategy.createEmptyContext();
        ctx.setAuthentication(authResult);

        this.securityContextHolderStrategy.setContext(ctx);
        this.securityContextRepository.saveContext(ctx, httpRequest, httpResponse);
    }
}
