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
import nextstep.security.converter.AuthenticationConverter;
import nextstep.security.exception.AuthenticationException;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class UsernamePasswordAuthenticationFilter extends GenericFilterBean {
    private static final String DEFAULT_FILTER_PROCESS_URL = "/login";

    private final AuthenticationManager authenticationManager;
    private final AuthenticationConverter authenticationConverter;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;

    public UsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager,
                                                AuthenticationConverter authenticationConverter,
                                                AuthenticationSuccessHandler authenticationSuccessHandler,
                                                AuthenticationFailureHandler authenticationFailureHandler) {

        this.authenticationManager = authenticationManager;
        this.authenticationConverter = authenticationConverter;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
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

            authenticationSuccessHandler.onAuthenticationSuccess(httpRequest, httpResponse, authResult);
        } catch (AuthenticationException e) {
            authenticationFailureHandler.onAuthenticationFailure(httpRequest, httpResponse, e);
        }
    }

    private boolean requireAuthentication(HttpServletRequest httpRequest) {
        return httpRequest.getRequestURI().equals(DEFAULT_FILTER_PROCESS_URL)
                && httpRequest.getMethod().equals("POST");
    }

}
