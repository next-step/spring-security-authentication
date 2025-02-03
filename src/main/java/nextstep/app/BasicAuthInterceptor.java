package nextstep.app;

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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            String authType = authorizationHeader.split(" ")[0];
            String credentials = authorizationHeader.split(" ")[1];
            String decodedString = Base64Convertor.decode(credentials);
            checkAuthType(authType);

            String[] usernameAndPassword = decodedString.split(":");
            String username = usernameAndPassword[0];
            String password = usernameAndPassword[1];

            memberRepository.findByEmail(username)
                    .filter(it -> it.matchPassword(password))
                    .orElseThrow(AuthenticationException::new);
            return true;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }

    private void checkAuthType(String authType) {
        if (!authType.equalsIgnoreCase(HttpServletRequest.BASIC_AUTH)) {
            throw new AuthenticationException();
        }
    }

}
