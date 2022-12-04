package nextstep.app.ui;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.app.ui.dto.LoginRequest;
import nextstep.security.exception.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private final MemberRepository memberRepository;

    public LoginController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@ModelAttribute LoginRequest request) {
        final String email = request.getUsername();
        final String password = request.getPassword();

        final Member member = memberRepository.findByEmail(email)
            .orElseThrow(AuthenticationException::new);

        if (!member.isSamePassword(password)) {
            throw new AuthenticationException();
        }

        return ResponseEntity.ok()
            .build();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> handleAuthenticationException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .build();
    }
}
