package nextstep.security.authentication;

import static nextstep.security.authentication.SecurityConstants.SPRING_SECURITY_CONTEXT_KEY;

import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import nextstep.security.exception.AuthenticationException;
import nextstep.security.userdetail.UserDetail;
import nextstep.security.userdetail.UserDetailService;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;

public class FormLoginAuthInterceptor implements HandlerInterceptor {

    private final UserDetailService userDetailService;

    public FormLoginAuthInterceptor(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
        try {
            HttpSession session = request.getSession();

            if (session.getAttribute(SPRING_SECURITY_CONTEXT_KEY) != null) {
                session.removeAttribute(SPRING_SECURITY_CONTEXT_KEY);
            }

            String username = request.getParameter("username");
            String password = request.getParameter("password");

            if (ObjectUtils.isEmpty(username) || ObjectUtils.isEmpty(password)) {
                throw new AuthenticationException();
            }

            UserDetail userDetail = userDetailService.getUserDetail(username);

            if (Objects.isNull(userDetail)) {
                throw new AuthenticationException();
            }

            if (!userDetail.verifyPassword(password)) {
                throw new AuthenticationException();
            }

            request.getSession().setAttribute(SPRING_SECURITY_CONTEXT_KEY, userDetail);

            return true;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }
}
