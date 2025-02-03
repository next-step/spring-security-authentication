package nextstep.app;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.app.ui.AuthenticationException;
import org.springframework.web.servlet.HandlerInterceptor;

public class FormLoginInterceptor implements HandlerInterceptor {

    private static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";

    private final MemberRepository memberRepository;

    public FormLoginInterceptor(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            Member member = memberRepository.findByEmail(username)
                    .filter(it -> it.matchPassword(password))
                    .orElseThrow(AuthenticationException::new);

            addMemberToSession(request, member);
        }catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        return false;
    }

    private void addMemberToSession(HttpServletRequest request, Member member) {
        HttpSession session = request.getSession();
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, member);
    }

}
