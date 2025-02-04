package nextstep.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.Authentication;
import nextstep.security.AuthenticationConverter;
import nextstep.security.AuthenticationManager;
import nextstep.security.SecurityContext;
import nextstep.security.SecurityContextRepository;
import nextstep.security.context.SecurityContextHolder;
import nextstep.security.exception.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class BasicAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(BasicAuthenticationFilter.class);

    private final AuthenticationManager authenticationManager;
    private final AuthenticationConverter authenticationConverter;
    private final SecurityContextRepository securityContextRepository;

    public BasicAuthenticationFilter(AuthenticationManager authenticationManager,
                                     AuthenticationConverter authenticationConverter,
                                     SecurityContextRepository securityContextRepository) {

        this.authenticationManager = authenticationManager;
        this.authenticationConverter = authenticationConverter;
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

            SecurityContext ctx = setSecurityContext(authResult);
            securityContextRepository.saveContext(ctx, request, response);

        } catch (AuthenticationException e) {
            logger.error("Basic Authentication failed", e);
            SecurityContextHolder.clearContext();

            response.addHeader("WWW-Authenticate", "Basic realm=\"nextstep\"");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    private SecurityContext setSecurityContext(Authentication authResult) {
        SecurityContext ctx = SecurityContextHolder.createEmptyContext();
        ctx.setAuthentication(authResult);
        SecurityContextHolder.setContext(ctx);
        return ctx;
    }
}
