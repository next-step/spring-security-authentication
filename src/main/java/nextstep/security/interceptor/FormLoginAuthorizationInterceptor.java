package nextstep.security.interceptor;

import nextstep.security.param.UserDetails;
import nextstep.security.service.UserDetailsService;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static nextstep.security.constants.SecurityConstants.*;

public class FormLoginAuthorizationInterceptor implements HandlerInterceptor {
    private final UserDetailsService userDetailService;

    public FormLoginAuthorizationInterceptor(UserDetailsService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String username = request.getParameter(USERNAME);
        String password = request.getParameter(PASSWORD);

        if (username == null || password == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "username, password가 필요합니다.");
            return false;
        }

        UserDetails userDetails = userDetailService.retrieveMemberByEmailAndPassword(username, password);
        if (userDetails == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "username, password가 일치하지 않슴니다.");
            return false;
        }

        request.getSession().setAttribute(SPRING_SECURITY_CONTEXT_KEY, userDetails);
        return true;
    }
}
