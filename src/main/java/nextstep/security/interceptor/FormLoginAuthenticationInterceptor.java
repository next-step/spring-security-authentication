package nextstep.security.interceptor;

import nextstep.security.service.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class FormLoginAuthenticationInterceptor implements HandlerInterceptor {
    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";

    private final UserDetailsService userDetailsService;

    public FormLoginAuthenticationInterceptor(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();

        if (session == null || session.getAttribute(SPRING_SECURITY_CONTEXT_KEY) == null) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            userDetailsService.authenticate(username, password);
        }

        return true;
    }
}
