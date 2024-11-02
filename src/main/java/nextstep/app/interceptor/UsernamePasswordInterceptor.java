package nextstep.app.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextstep.app.constants.AppConstants;
import nextstep.app.domain.Member;
import nextstep.app.service.MemberService;
import nextstep.app.ui.AuthenticationException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static nextstep.app.constants.AppConstants.SPRING_SECURITY_CONTEXT_KEY;

@Slf4j
@RequiredArgsConstructor
public class UsernamePasswordInterceptor implements HandlerInterceptor {
    private final MemberService memberService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HttpSession session = request.getSession();

        if (session.getAttribute(SPRING_SECURITY_CONTEXT_KEY) == null) {
            String username = request.getParameter(AppConstants.USERNAME);
            String password = request.getParameter(AppConstants.PASSWORD);

            Member member = memberService.getMember(username, password);

            if (member == null) {
                throw new AuthenticationException();
            }

            session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, member);
        }

        return true;
    }
}
