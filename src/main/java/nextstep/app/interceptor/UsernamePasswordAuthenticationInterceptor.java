package nextstep.app.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.app.ui.AuthenticationException;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

public class UsernamePasswordAuthenticationInterceptor implements HandlerInterceptor {

    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";

    private final MemberRepository memberRepository;

    public UsernamePasswordAuthenticationInterceptor(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<String, String[]> parameterMap = request.getParameterMap();
        String username = parameterMap.get("username")[0];
        String password = parameterMap.get("password")[0];

        Member member = memberRepository.findByEmail(username)
                .filter(it -> it.matchPassword(password))
                .orElseThrow(AuthenticationException::new);

        request.getSession().setAttribute(SPRING_SECURITY_CONTEXT_KEY, member);

        return true;
    }
}
