package nextstep.app.ui;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.app.support.Authentication;
import nextstep.app.support.SecurityContextHolder;
import nextstep.app.ui.dto.LoginRequest;
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
        SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());

        final String email = request.getUsername();
        final String password = request.getPassword();

        final Member member = memberRepository.findByEmail(email)
            .orElseThrow(AuthenticationException::new);

        if (!member.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        SecurityContextHolder.getContext().setAuthentication(
            new Authentication() {
                @Override
                public Object getPrincipal() {
                    return null;
                }

                @Override
                public Object getCredentials() {
                    return null;
                }

                @Override
                public boolean isAuthenticated() {
                    return true;
                }
            }
        );

        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> handleAuthenticationException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
