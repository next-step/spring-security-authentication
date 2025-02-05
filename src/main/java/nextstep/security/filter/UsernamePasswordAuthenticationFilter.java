package nextstep.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
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
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class UsernamePasswordAuthenticationFilter extends GenericFilterBean {
    private static final Logger logger = LoggerFactory.getLogger(UsernamePasswordAuthenticationFilter.class);
    private static final String DEFAULT_FILTER_PROCESS_URL = "/login";

    private final AuthenticationManager authenticationManager;
    private final AuthenticationConverter authenticationConverter;
    private final SecurityContextRepository securityContextRepository;

    public UsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager,
                                                AuthenticationConverter authenticationConverter,
                                                SecurityContextRepository securityContextRepository) {

        this.authenticationManager = authenticationManager;
        this.authenticationConverter = authenticationConverter;
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

            onAuthenticationSuccess(httpRequest, httpResponse, authResult);
        } catch (AuthenticationException e) {
            onAuthenticationFailure(httpResponse, e);
        }
    }

    private boolean requireAuthentication(HttpServletRequest httpRequest) {
        return httpRequest.getRequestURI().equals(DEFAULT_FILTER_PROCESS_URL)
                && httpRequest.getMethod().equals("POST");
    }

    private void onAuthenticationSuccess(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
                                         Authentication authResult) {
        SecurityContext ctx = setSecurityContext(authResult);
        securityContextRepository.saveContext(ctx, httpRequest, httpResponse);
    }

    private void onAuthenticationFailure(HttpServletResponse httpResponse,
                                         AuthenticationException e) throws IOException {
        SecurityContextHolder.clearContext();
        httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
    }

    private SecurityContext setSecurityContext(Authentication authResult) {
        SecurityContext ctx = SecurityContextHolder.createEmptyContext();
        ctx.setAuthentication(authResult);
        SecurityContextHolder.setContext(ctx);
        return ctx;
    }

}
