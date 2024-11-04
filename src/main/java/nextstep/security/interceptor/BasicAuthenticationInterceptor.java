package nextstep.security.interceptor;

import nextstep.app.domain.MemberService;
import nextstep.security.exception.AuthenticationException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BasicAuthenticationInterceptor implements HandlerInterceptor {

    private final MemberService memberService;

    public BasicAuthenticationInterceptor(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String basicToken = request.getHeader("Authorization");
        try {
            memberService.validate(basicToken);
        }
        catch(AuthenticationException e){
            response.setStatus(e.getStatus().value());
            response.getWriter().println(String.format("\"%s\" : \"%s\"\n","message", e.getMessage()));
            return false;
        }
        return true;
    }
}
