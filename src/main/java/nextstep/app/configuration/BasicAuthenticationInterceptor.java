package nextstep.app.configuration;

import nextstep.app.domain.MemberService;
import nextstep.security.authentication.BasicAuthenticationToken;
import nextstep.security.support.BasicAuthenticationDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
class BasicAuthenticationInterceptor implements HandlerInterceptor {

    private final MemberService memberService;

    private final BasicAuthenticationDecoder basicAuthenticationDecoder;

    public BasicAuthenticationInterceptor(MemberService memberService, BasicAuthenticationDecoder basicAuthenticationDecoder) {
        this.memberService = memberService;
        this.basicAuthenticationDecoder = basicAuthenticationDecoder;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        BasicAuthenticationToken basicAuthentication = basicAuthenticationDecoder.decode(request);

        memberService.validateMember(
                basicAuthentication.getPrincipal().toString(),
                basicAuthentication.getCredentials().toString()
        );

        return true;
    }
}
