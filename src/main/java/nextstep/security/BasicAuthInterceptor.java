package nextstep.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.app.domain.MemberRepository;
import nextstep.app.util.Base64Convertor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class BasicAuthInterceptor implements HandlerInterceptor {

    private final MemberRepository memberRepository;

    private static final String AUTHORIZATION_HEADER = "Authorization";

    public BasicAuthInterceptor(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorization = request.getHeader(AUTHORIZATION_HEADER);
        if (!StringUtils.hasText(authorization)) {
            throw new AuthenticationException();
        }

        String credentials = authorization.split(" ")[1];
        String decodedString = Base64Convertor.decode(credentials);
        String[] usernameAndPassword = decodedString.split(":");
        if (usernameAndPassword.length != 2) {
            throw new AuthenticationException();
        }

        String username = usernameAndPassword[0];
        String password = usernameAndPassword[1];

        memberRepository.findByEmail(username)
                .filter(it -> it.matchPassword(password))
                .orElseThrow(AuthenticationException::new);

        return true;
    }
}
