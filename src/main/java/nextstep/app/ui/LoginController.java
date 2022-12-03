package nextstep.app.ui;

import nextstep.app.domain.MemberRepository;
import nextstep.security.AuthenticationException;
import nextstep.security.authentication.FormAuthentication;
import nextstep.security.support.SecurityContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LoginController {
    private final MemberRepository memberRepository;

    public LoginController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(HttpServletRequest request) {
        SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());

        final var email = request.getParameter("username");
        final var password = request.getParameter("password");

        final var member = memberRepository.findByEmail(email)
                .orElseThrow(AuthenticationException::new);

        final var authentication = new FormAuthentication(member.equalsPassword(password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return ResponseEntity.ok().build();
    }
}
