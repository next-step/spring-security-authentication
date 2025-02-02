package nextstep.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.security.UserDetails;
import nextstep.security.UserDetailsService;
import nextstep.security.exception.AuthenticationException;

import java.io.IOException;
import java.util.Map;

public class FormAuthFilter extends CustomSecurityAuthFilter {
    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";
    private final UserDetailsService userDetailsService;

    public FormAuthFilter(final UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    boolean match(final HttpServletRequest request) {
        return request.getRequestURI().equals("/login");
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        if (!match(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Map<String, String[]> parameterMap = request.getParameterMap();
            String username = parameterMap.get("username")[0];
            String password = parameterMap.get("password")[0];

            final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (!userDetails.getPassword().equals(password)) {
                throw new AuthenticationException();
            }

            HttpSession session = request.getSession();
            session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, userDetails);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
