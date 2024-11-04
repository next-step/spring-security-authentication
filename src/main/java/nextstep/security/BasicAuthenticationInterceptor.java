package nextstep.security;

import nextstep.app.domain.LoginService;
import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.app.ui.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Component
public class BasicAuthenticationInterceptor implements HandlerInterceptor {

    private final LoginService loginService;
    private final MemberRepository memberRepository;

    public BasicAuthenticationInterceptor(LoginService loginService, MemberRepository memberRepository) {
        this.loginService = loginService;
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        System.out.println("BasicAuthenticationInterceptor.preHandle");

        String authHeader = request.getHeader("Authorization");

        String[] split = authHeader.split(" ");
        String type = split[0];
        String credential = split[1];

        System.out.println("type = " + type);
        System.out.println("credential = " + credential);

        try {
            String decodedCredential = new String(org.springframework.util.Base64Utils.decodeFromString(credential));

            String[] emailAndPassword = decodedCredential.split(":");
            String email = emailAndPassword[0];
            String password = emailAndPassword[1];

            // 사용자 인증 검증
            Member authenticatedMember = memberRepository.findByEmail(email).orElse(null);
            if (authenticatedMember == null) {
                throw new AuthenticationException();
            }

            List<Member> members = memberRepository.findAll();
            return true;
        } catch (Exception e) {
            throw new AuthenticationException();
        }
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> handleAuthenticationException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
