package nextstep.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.security.authentication.*;
import nextstep.security.exception.AuthenticationException;
import nextstep.security.user.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class FormLoginFilter extends OncePerRequestFilter {
    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";
    private static final String MATCH_URI = "/login";

    private final AuthenticationManager authenticationManager;

    public FormLoginFilter(UserDetailsService userDetailsService) {
        this.authenticationManager = new ProviderManager(List.of(new DaoAuthenticationProvider(userDetailsService)));
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

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            Authentication authenticate = authenticationManager.authenticate(authenticationToken);

            if (!authenticate.isAuthenticated()) {
                throw new AuthenticationException();
            }
            HttpSession session = request.getSession();
            session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, authenticate);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}