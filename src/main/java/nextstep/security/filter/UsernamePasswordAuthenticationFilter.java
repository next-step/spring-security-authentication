package nextstep.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
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
import nextstep.security.util.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class UsernamePasswordAuthenticationFilter extends GenericFilterBean {
    private final SecurityContextHolderStrategy securityContextHolderStrategy =
            SecurityContextHolder.getContextHolderStrategy();

    private final RequestMatcher requestMatcher;
    private final AuthenticationManager authenticationManager;
    private final AuthenticationConverter authenticationConverter;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final SecurityContextRepository securityContextRepository;

    public UsernamePasswordAuthenticationFilter(RequestMatcher requestMatcher,
                                                AuthenticationManager authenticationManager,
                                                AuthenticationConverter authenticationConverter,
                                                AuthenticationSuccessHandler authenticationSuccessHandler,
                                                AuthenticationFailureHandler authenticationFailureHandler,
                                                SecurityContextRepository securityContextRepository) {

        this.requestMatcher = requestMatcher;
        this.authenticationManager = authenticationManager;
        this.authenticationConverter = authenticationConverter;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.securityContextRepository = securityContextRepository;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (!requireAuthentication(httpRequest)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Authentication authRequest = this.authenticationConverter.convert(httpRequest);
            Authentication authResult = this.authenticationManager.authenticate(authRequest);

            saveSecurityContext(authResult, httpRequest, httpResponse);
            this.authenticationSuccessHandler.onAuthenticationSuccess(httpRequest, httpResponse, authResult);
        } catch (AuthenticationException e) {
            this.securityContextHolderStrategy.clearContext();

            this.authenticationFailureHandler.onAuthenticationFailure(httpRequest, httpResponse, e);
        }
    }

    private boolean requireAuthentication(HttpServletRequest httpRequest) {
        return requestMatcher.matches(httpRequest);
    }

    private void saveSecurityContext(Authentication authResult, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        SecurityContext ctx = this.securityContextHolderStrategy.createEmptyContext();
        ctx.setAuthentication(authResult);

        this.securityContextHolderStrategy.setContext(ctx);
        this.securityContextRepository.saveContext(ctx, httpRequest, httpResponse);
    }

}
