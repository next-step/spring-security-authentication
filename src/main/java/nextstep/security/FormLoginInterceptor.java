package nextstep.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class FormLoginInterceptor implements HandlerInterceptor {

    private static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";

    private final UserDetailService userDetailService;

    public FormLoginInterceptor(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            UserDetails userDetail = userDetailService.getUserByUsername(username);
            if (!userDetail.getPassword().equals(password)) {
                throw new AuthenticationException();
            }

            addMemberToSession(request, userDetail);
        }catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        return false;
    }

    private void addMemberToSession(HttpServletRequest request, UserDetails userDetail) {
        HttpSession session = request.getSession();
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, userDetail);
    }

}
