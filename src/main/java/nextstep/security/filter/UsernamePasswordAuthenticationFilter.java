package nextstep.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.security.Authentication;
import nextstep.security.AuthenticationConverter;
import nextstep.security.AuthenticationEntrypoint;
import nextstep.security.AuthenticationManager;
import nextstep.security.exception.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class UsernamePasswordAuthenticationFilter extends GenericFilterBean {
    private static final Logger logger = LoggerFactory.getLogger(UsernamePasswordAuthenticationFilter.class);
    private static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";
    private static final String DEFAULT_FILTER_PROCESS_URL = "/login";

    private final AuthenticationManager authenticationManager;
    private final AuthenticationConverter authenticationConverter;
    private final AuthenticationEntrypoint authenticationEntrypoint;

    public UsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager,
                                                AuthenticationConverter authenticationConverter,
                                                AuthenticationEntrypoint authenticationEntrypoint) {

        this.authenticationManager = authenticationManager;
        this.authenticationConverter = authenticationConverter;
        this.authenticationEntrypoint = authenticationEntrypoint;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        if (!requireAuthentication(httpRequest)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Authentication authRequest = this.authenticationConverter.convert(httpRequest);

            if (authRequest == null) {
                filterChain.doFilter(request, response);
                return;
            }

            Authentication authResult = this.authenticationManager.authenticate(authRequest);

            HttpSession session = httpRequest.getSession();
            session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, authResult);
        } catch (AuthenticationException e) {
            logger.error("UsernamePassword Authentication failed", e);
            authenticationEntrypoint.commence((HttpServletRequest) request, (HttpServletResponse) response, e);
        }
    }

    private boolean requireAuthentication(HttpServletRequest httpRequest) {
        return httpRequest.getRequestURI().equals(DEFAULT_FILTER_PROCESS_URL);
    }

}
