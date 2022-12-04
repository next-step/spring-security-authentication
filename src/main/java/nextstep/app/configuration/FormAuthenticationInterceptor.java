package nextstep.app.configuration;

import nextstep.app.domain.MemberService;
import nextstep.security.authentication.FormAuthenticationProvider;
import nextstep.security.support.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
class FormAuthenticationInterceptor implements HandlerInterceptor {

    private final MemberService memberService;

    private final FormAuthenticationProvider formAuthenticationProvider;

    FormAuthenticationInterceptor(MemberService memberService, FormAuthenticationProvider formAuthenticationProvider) {
        this.memberService = memberService;
        this.formAuthenticationProvider = formAuthenticationProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());

        Map<String, String[]> paramMap = request.getParameterMap();
        String email = paramMap.get("username")[0];
        String password = paramMap.get("password")[0];

        memberService.validateMember(email, password);

        formAuthenticationProvider.doAuthentication(email, password);

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
