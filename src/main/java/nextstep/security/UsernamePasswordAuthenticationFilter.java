package nextstep.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.app.ui.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

public class UsernamePasswordAuthenticationFilter extends OncePerRequestFilter {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";

    private final UserDetailsService userDetailsService;

    public UsernamePasswordAuthenticationFilter(final UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        try {
            checkAuthentication(request, response);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private void checkAuthentication(final HttpServletRequest request, final HttpServletResponse response) {
        final Map<String, String[]> parameterMap = request.getParameterMap();
        final String[] userNameParams = parameterMap.get(USERNAME);
        final String[] passwordParams = parameterMap.get(PASSWORD);

        if (userNameParams == null || passwordParams == null) {
            return;
        }

        final String username = userNameParams[0];
        final String password = passwordParams[0];

        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        checkValidUserDetails(userDetails, password);

        final HttpSession session = request.getSession();
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, userDetails);
    }

    private void checkValidUserDetails(final UserDetails userDetails, final String password) {
        if (!userDetails.getPassword().equals(password)) {
            throw new AuthenticationException();
        }
    }
}
