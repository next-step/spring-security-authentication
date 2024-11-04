package nextstep.security.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextstep.security.model.UserDetails;
import nextstep.security.service.UserDetailService;
import nextstep.app.ui.AuthenticationException;
import nextstep.security.constants.SecurityConstants;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@RequiredArgsConstructor
@Component
public class UsernamePasswordInterceptor implements HandlerInterceptor {
    private final UserDetailService userDetailService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HttpSession session = request.getSession();

        if (session.getAttribute(SecurityConstants.SPRING_SECURITY_CONTEXT_KEY) == null) {
            String username = request.getParameter(SecurityConstants.USERNAME);
            String password = request.getParameter(SecurityConstants.PASSWORD);

            UserDetails userDetails = userDetailService.getUserDetails(username, password);

            if (userDetails == null) {
                throw new AuthenticationException();
            }

            session.setAttribute(SecurityConstants.SPRING_SECURITY_CONTEXT_KEY, userDetails);
        }

        return true;
    }
}
