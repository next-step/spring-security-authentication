package nextstep.security;

import static nextstep.security.SecurityConstants.SPRING_SECURITY_CONTEXT_KEY;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.app.ui.AuthenticationException;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.DefaultAuthentication;
import org.springframework.web.filter.OncePerRequestFilter;

public class UsernamePasswordAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    private final List<String> ACCEPTED_URIS = List.of(
            "/login"
    );

    public UsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        if (!ACCEPTED_URIS.contains(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Map<String, String[]> paramMap = request.getParameterMap();

            Authentication authentication = authenticationManager.authenticate(
                    createAuthentication(paramMap));

            validateAuthentication(authentication);

            request.getSession().setAttribute(SPRING_SECURITY_CONTEXT_KEY, authentication);

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private Authentication createAuthentication(Map<String, String[]> paramMap) {
        return DefaultAuthentication.unauthenticated(
                paramMap.get("username")[0],
                paramMap.get("password")[0]);
    }

    private void validateAuthentication(Authentication authentication) {
        if (authentication == null) {
            throw new AuthenticationException();
        }

        if (!authentication.isAuthenticated()) {
            throw new AuthenticationException();
        }
    }

}
