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

import static nextstep.app.constants.AppConstants.SPRING_SECURITY_CONTEXT_KEY;

@RequiredArgsConstructor
@RestController
public class LoginController {
    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<Void> login() {
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> handleAuthenticationException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
