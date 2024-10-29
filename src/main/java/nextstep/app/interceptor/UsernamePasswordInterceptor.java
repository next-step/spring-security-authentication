package nextstep.app.interceptor;

import nextstep.app.domain.MemberRepository;
import nextstep.app.ui.AuthenticationException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class UsernamePasswordInterceptor implements HandlerInterceptor {
    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";

    private final MemberRepository memberRepository;

    public UsernamePasswordInterceptor(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();

        if (session == null || session.getAttribute(SPRING_SECURITY_CONTEXT_KEY) == null) {
            authenticate(request);
        }

        return true;
    }

    private void authenticate(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        memberRepository.findByEmail(username)
                .filter(member -> member.getPassword().equals(password))
                .ifPresentOrElse(
                        member -> request.getSession().setAttribute(SPRING_SECURITY_CONTEXT_KEY, member),
                        () -> { throw new AuthenticationException(); }
                );
    }
}
