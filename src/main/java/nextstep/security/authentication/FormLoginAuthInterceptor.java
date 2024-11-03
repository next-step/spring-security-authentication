package nextstep.security.authentication;

import static nextstep.security.util.SecurityConstants.SPRING_SECURITY_CONTEXT_KEY;

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
            validateParamAndSession(request);

            String username = request.getParameter("username");
            String password = request.getParameter("password");

            UserDetail userDetail = userDetailService.getUserDetail(username);
            verifyUserDetail(userDetail, password);
            request.getSession().setAttribute(SPRING_SECURITY_CONTEXT_KEY, userDetail);

            return true;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }

    private void validateParamAndSession(HttpServletRequest request) {
        HttpSession session = request.getSession();

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (session.getAttribute(SPRING_SECURITY_CONTEXT_KEY) != null) {
            session.removeAttribute(SPRING_SECURITY_CONTEXT_KEY);
        }

        if (ObjectUtils.isEmpty(username) || ObjectUtils.isEmpty(password)) {
            throw new AuthenticationException();
        }
    }

    private void verifyUserDetail(UserDetail userDetail, String password) {
        if (Objects.isNull(userDetail)) {
            throw new AuthenticationException();
        }

        if (!userDetail.verifyPassword(password)) {
            throw new AuthenticationException();
        }
    }
}
