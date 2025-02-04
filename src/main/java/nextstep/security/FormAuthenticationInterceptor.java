package nextstep.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.app.ui.AuthenticationException;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

public class FormAuthenticationInterceptor implements HandlerInterceptor {

    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";

    private final UserDetailService userDetailService;

    public FormAuthenticationInterceptor(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();

        Map<String, String[]> parameterMap = request.getParameterMap();
        String username = parameterMap.get("username")[0];
        String password = parameterMap.get("password")[0];

        UserDetail userDetail = userDetailService.loadUserByUsername(username);
        if (!userDetail.getPassword().equals(password)) {
            throw new AuthenticationException();
        }
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, userDetail);

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
