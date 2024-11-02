package nextstep.security.interceptor;

import nextstep.app.domain.Member;
import nextstep.app.domain.UserDetailService;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FormLoginAuthorizationInterceptor implements HandlerInterceptor {
    private final UserDetailService userDetailService;

    public FormLoginAuthorizationInterceptor(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || password == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "username, password가 필요합니다.");
            return false;
        }

        Member member = userDetailService.retrieveMemberByEmailAndPassword(username, password);
        if (member == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "username, password가 일치하지 않슴니다.");
            return false;
        }

        request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", member);
        return true;
    }
}
