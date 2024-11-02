package nextstep.app.ui;

import nextstep.app.constants.AppConstants;
import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class LoginController {
    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";

    private final MemberRepository memberRepository;

    public LoginController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(HttpServletRequest request, HttpSession session) {
        String username = request.getParameter(AppConstants.USERNAME);
        String password = request.getParameter(AppConstants.PASSWORD);

        Member member = getMember(username, password);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, member);

        return ResponseEntity.ok().build();
    }

    private Member getMember(String username, String password) {
        Member member = memberRepository.findByEmail(username).orElse(null);

        if (member == null) {
            throw new AuthenticationException();
        }

        if (!StringUtils.equals(member.getPassword(), password)) {
            throw new AuthenticationException();
        }

        return member;
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> handleAuthenticationException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
