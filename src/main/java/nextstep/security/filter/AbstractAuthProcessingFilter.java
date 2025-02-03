package nextstep.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationManager;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public abstract class AbstractAuthProcessingFilter extends OncePerRequestFilter {
    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";
    private final AuthenticationManager authenticationManager;

    protected AbstractAuthProcessingFilter(final AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    abstract boolean match(final HttpServletRequest request);

    abstract Authentication makeAuthentication(final HttpServletRequest request);

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        if (!match(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Authentication authRequest = makeAuthentication(request);

            Authentication authenticationResult = this.authenticationManager.authenticate(authRequest);

            saveAuthentication(request, authenticationResult);

            successAuthentication(request, response, filterChain);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    protected abstract void successAuthentication(final HttpServletRequest request, final HttpServletResponse response,
                                                  final FilterChain filterChain) throws ServletException, IOException;

    private static void saveAuthentication(final HttpServletRequest request, final Authentication authenticationResult) {
        HttpSession session = request.getSession();
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, authenticationResult);
    }
}
