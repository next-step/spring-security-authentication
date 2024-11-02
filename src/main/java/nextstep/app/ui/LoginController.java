package nextstep.app.ui;

import lombok.RequiredArgsConstructor;
import nextstep.app.constants.AppConstants;
import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.app.service.MemberService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@RestController
public class LoginController {
    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";

    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(HttpServletRequest request, HttpSession session) {
        String username = request.getParameter(AppConstants.USERNAME);
        String password = request.getParameter(AppConstants.PASSWORD);

        Member member = memberService.getMember(username, password);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, member);

        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> handleAuthenticationException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
