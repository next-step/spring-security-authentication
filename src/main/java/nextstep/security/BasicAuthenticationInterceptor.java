package nextstep.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.util.Base64Convertor;
import org.springframework.web.servlet.HandlerInterceptor;

public class BasicAuthenticationInterceptor implements HandlerInterceptor {

    private final MemberDetailService memberDetailService;

    public BasicAuthenticationInterceptor(MemberDetailService memberDetailService) {
        this.memberDetailService = memberDetailService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            String authorization = request.getHeader("Authorization");
            String credentials = authorization.split(" ")[1];
            String decodedString = Base64Convertor.decode(credentials);
            String[] usernameAndPassword = decodedString.split(":");
            String username = usernameAndPassword[0];
            String password = usernameAndPassword[1];

            MemberDetail member = memberDetailService.findByUsername(username);
            if (!member.isCorrectPassword(password)) {
                throw new AuthenticationException();
            }

        } catch (Exception e) {
            throw new AuthenticationException();
        }

        return true;
    }
}
