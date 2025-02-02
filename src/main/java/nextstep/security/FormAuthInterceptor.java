package nextstep.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

public class FormAuthInterceptor implements HandlerInterceptor {
    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";
    private final UserDetailsService userDetailsService;

    public FormAuthInterceptor(final UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
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
            throw new AuthenticationException();
        }

        return false;
    }
}
