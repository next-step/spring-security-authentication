package nextstep.app.interceptor;

import nextstep.app.domain.MemberService;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationToken;
import nextstep.security.authentication.provider.AuthenticationProvider;
import nextstep.security.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class LoginAuthenticationInterceptor implements HandlerInterceptor {

    private final MemberService memberService;
    private final AuthenticationProvider authenticationProvider;

    public LoginAuthenticationInterceptor(MemberService memberService, AuthenticationProvider authenticationProvider) {
        this.memberService = memberService;
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());

        Map<String, String[]> paramMap = request.getParameterMap();
        String email = paramMap.get("username")[0];
        String password = paramMap.get("password")[0];

        memberService.isValidMemberInfo(email, password);

        Authentication authentication = new AuthenticationToken(email, password);
        authenticationProvider.authenticate(authentication);

        return true;
    }
}
