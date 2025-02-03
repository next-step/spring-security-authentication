package nextstep.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

public class UsernamePasswordAuthenticationInterceptor implements HandlerInterceptor {

    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";

    private final MemberDetailService memberDetailService;

    public UsernamePasswordAuthenticationInterceptor(MemberDetailService memberDetailService) {
        this.memberDetailService = memberDetailService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<String, String[]> parameterMap = request.getParameterMap();
        String username = parameterMap.get("username")[0];
        String password = parameterMap.get("password")[0];

        MemberDetail member = memberDetailService.findByUsername(username);
        if (!member.isCorrectPassword(password)) {
            throw new AuthenticationException();
        }

        request.getSession().setAttribute(SPRING_SECURITY_CONTEXT_KEY, member);
        return true;
    }
}
