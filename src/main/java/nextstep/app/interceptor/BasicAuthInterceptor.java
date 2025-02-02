package nextstep.app.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.app.domain.MemberRepository;
import nextstep.app.ui.AuthenticationException;
import nextstep.app.util.Base64Convertor;
import org.springframework.web.servlet.HandlerInterceptor;

public class BasicAuthInterceptor implements HandlerInterceptor {
    private final MemberRepository memberRepository;

    public BasicAuthInterceptor(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        String authorization = request.getHeader("Authorization");

        try {
            String credentials = authorization.split(" ")[1];
            String decodedString = Base64Convertor.decode(credentials);
            String[] usernameAndPassword = decodedString.split(":");
            String username = usernameAndPassword[0];
            String password = usernameAndPassword[1];

            memberRepository.findByEmail(username)
                    .filter(it -> it.matchPassword(password))
                    .orElseThrow(AuthenticationException::new);

            return true;
        } catch (Exception e) {
            throw new AuthenticationException();
        }
    }
}
