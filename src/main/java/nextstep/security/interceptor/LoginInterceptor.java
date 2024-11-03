package nextstep.security.interceptor;

import nextstep.app.domain.Member;
import nextstep.security.service.MemberRepository;
import nextstep.security.exception.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static nextstep.security.SecurityConstants.SPRING_SECURITY_CONTEXT_KEY;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    private final MemberRepository memberRepository;

    public LoginInterceptor(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("로그인 인터셉터");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || password == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        Member member = memberRepository.findByEmail(username)
                .orElseThrow(AuthenticationException::new);

        if (!password.equals(member.getPassword())) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // 세션에 인증 정보 저장
        HttpSession session = request.getSession();
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, member);

        return true; // 다음으로 요청을 진행
    }
}
