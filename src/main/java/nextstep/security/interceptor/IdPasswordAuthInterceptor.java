package nextstep.security.interceptor;

import nextstep.security.model.UserDetail;
import nextstep.security.service.UserDetailService;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class IdPasswordAuthInterceptor implements HandlerInterceptor {
    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";
    private final UserDetailService userDetailService;

    public IdPasswordAuthInterceptor(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userName = request.getParameter("username");
        String passWord = request.getParameter("password");

        UserDetail userDetail = userDetailService.isValidUser(userName, passWord);

        HttpSession session = request.getSession();
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, userDetail);
        return true;
    }
}
