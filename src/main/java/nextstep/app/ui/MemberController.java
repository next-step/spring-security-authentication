package nextstep.app.ui;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static nextstep.app.ui.LoginController.SPRING_SECURITY_CONTEXT_KEY;

@RestController
public class MemberController {

    private final MemberRepository memberRepository;

    public MemberController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @GetMapping("/members")
    public ResponseEntity<List<Member>> list(HttpServletRequest request, HttpSession session) {
        authenticate(request, session);

        List<Member> members = memberRepository.findAll();
        return ResponseEntity.ok(members);
    }

    private void authenticate(HttpServletRequest request, HttpSession session) {
        String authorizationHeader = request.getHeader("Authorization");

        if (!isValidAuthorizationHeader(authorizationHeader)) {
            throw new AuthenticationException();
        }

        String[] credentials = parseCredentials(authorizationHeader);
        String username = credentials[0];
        String password = credentials[1];

        memberRepository.findByEmail(username)
                .filter(member -> member.getPassword().equals(password))
                .ifPresentOrElse(
                        member -> session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, member),
                        () -> { throw new AuthenticationException(); }
                );
    }

    private boolean isValidAuthorizationHeader(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith("Basic ");
    }

    private String[] parseCredentials(String authorizationHeader) {
        String base64Credentials = authorizationHeader.substring("Basic ".length());
        String credentials = new String(Base64Utils.decodeFromString(base64Credentials), StandardCharsets.UTF_8);

        String[] values = credentials.split(":", 2);
        if (values.length != 2) {
            throw new AuthenticationException();
        }
        return values;
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> handleAuthenticationException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
