package nextstep.app.ui;

import nextstep.app.domain.MemberRepository;
import nextstep.app.support.FormAuthentication;
import nextstep.app.support.SecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class LoginController {
    private final MemberRepository memberRepository;

    public LoginController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());

        final var email = request.getParameter("username");
        final var password = request.getParameter("password");

        final var member = memberRepository.findByEmail(email)
                .orElseThrow(AuthenticationException::new);

        final var authentication = new FormAuthentication(member.isAuthenticated(password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> handleAuthenticationException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
