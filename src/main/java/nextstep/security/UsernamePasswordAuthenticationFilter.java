package nextstep.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.app.ui.AuthenticationException;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.DaoAuthenticationProvider;
import nextstep.security.authentication.ProviderManager;
import nextstep.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class UsernamePasswordAuthenticationFilter extends OncePerRequestFilter {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";

    private final AuthenticationManager authenticationManager;

    public UsernamePasswordAuthenticationFilter(final UserDetailsService userDetailsService) {
        this.authenticationManager = new ProviderManager(
                List.of(new DaoAuthenticationProvider(userDetailsService))
        );
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        try {
            final Authentication authentication = generateAuthentication(request, response);

            if (authentication == null) {
                filterChain.doFilter(request, response);
                return;
            }

            final Authentication authenticate = authenticationManager.authenticate(authentication);

            final HttpSession session = request.getSession();
            session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, authenticate);

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private Authentication generateAuthentication(final HttpServletRequest request, final HttpServletResponse response) {
        final Map<String, String[]> parameterMap = request.getParameterMap();
        final String[] userNameParams = parameterMap.get(USERNAME);
        final String[] passwordParams = parameterMap.get(PASSWORD);

        if (userNameParams == null || passwordParams == null) {
            return null;
        }

        final String username = userNameParams[0];
        final String password = passwordParams[0];

        return new UsernamePasswordAuthenticationToken(username, password, false);
    }
}
