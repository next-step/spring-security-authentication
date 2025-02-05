package nextstep.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.security.exception.AuthenticationException;
import nextstep.security.user.UserDetails;
import nextstep.security.user.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

public class FormLoginFilter extends OncePerRequestFilter {
    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";
    private static final String MATCH_URI = "/login";

    private final UserDetailsService userDetailsService;

    public FormLoginFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!request.getRequestURI().equals(MATCH_URI)) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            Map<String, String[]> parameterMap = request.getParameterMap();
            String username = parameterMap.get("username")[0];
            String password = parameterMap.get("password")[0];

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
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