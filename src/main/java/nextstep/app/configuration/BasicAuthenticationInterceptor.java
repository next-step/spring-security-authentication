package nextstep.app.configuration;

import nextstep.app.domain.MemberService;
import nextstep.security.exception.AuthenticationException;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
class BasicAuthenticationInterceptor implements HandlerInterceptor {

    private final MemberService memberService;

    public static final String AUTHORIZATION = "Basic";
    public static final String SPLIT_CHAR = ":";

    public BasicAuthenticationInterceptor(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(!authorization.startsWith(AUTHORIZATION)) {
            throw new AuthenticationException();
        }
        String decodeTarget = authorization.substring(AUTHORIZATION.length()).trim();
        String decodeValue = new String(Base64Utils.decode(decodeTarget.getBytes()));

        String[] emailAndPassword = decodeValue.split(SPLIT_CHAR);
        String email = emailAndPassword[0];
        String password = emailAndPassword[1];

        memberService.validateMember(email, password);

        return true;
    }
}
