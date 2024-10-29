package nextstep.app.interceptor;

import nextstep.app.domain.MemberRepository;
import nextstep.app.ui.AuthenticationException;
import org.springframework.util.Base64Utils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;


public class BasicAuthenticationInterceptor implements HandlerInterceptor {
    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";

    private final MemberRepository memberRepository;

    public BasicAuthenticationInterceptor(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        authenticate(request);
        return true;
    }

    private void authenticate(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (!isValidAuthorizationHeader(authorizationHeader)) {
            throw new AuthenticationException();
        }

        String[] credentials = parseCredentials(authorizationHeader);
        String username = credentials[0];
        String password = credentials[1];

        memberRepository.findByEmail(username)
                .filter(member -> member.getPassword().equals(password))
                .ifPresentOrElse(
                        member -> request.getSession().setAttribute(SPRING_SECURITY_CONTEXT_KEY, member),
                        () -> { throw new AuthenticationException(); }
                );
    }

    private boolean isValidAuthorizationHeader(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith("Basic ");
    }

    private String[] parseCredentials(String authorizationHeader) {
        String base64Credentials = authorizationHeader.substring("Basic ".length());
        String credentials = new String(Base64Utils.decodeFromString(base64Credentials), StandardCharsets.UTF_8);

        String[] values = credentials.split(":", 2);
        if (values.length != 2) {
            throw new AuthenticationException();
        }
        return values;
    }
}
