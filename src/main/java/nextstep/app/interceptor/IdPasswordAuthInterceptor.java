package nextstep.app.interceptor;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.app.ui.AuthenticationException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class IdPasswordAuthInterceptor implements HandlerInterceptor {
    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";
    private final MemberRepository memberRepository;

    public IdPasswordAuthInterceptor(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userName = request.getParameter("username");
        String passWord = request.getParameter("password");

        Member member = memberRepository.findByEmail(userName).orElseThrow(AuthenticationException::new);
        if (!member.getPassword().equals(passWord)) {
            throw new AuthenticationException();
        }

        HttpSession session = request.getSession();
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, member);
        return true;
    }
}
