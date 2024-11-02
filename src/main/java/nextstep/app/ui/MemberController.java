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

@RestController
public class MemberController {

    private final MemberRepository memberRepository;

    public MemberController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @GetMapping("/members")
    public ResponseEntity<List<Member>> list(HttpServletRequest request, HttpSession session) {
        List<Member> members = memberRepository.findAll();

        String basicToken = request.getHeader("Authorization");

        authenticate(basicToken);

        return ResponseEntity.ok(members);
    }

    private void authenticate(String token) {
        String payload = token.split(" ")[1];

        String decodedPayload = new String(Base64Utils.decodeFromString(payload), StandardCharsets.UTF_8);
        String[] memberInfo = decodedPayload.split(":");

        if (memberInfo.length != 2) {
            throw new AuthenticationException();
        }

        isValidMember(memberInfo[0], memberInfo[1]);
    }

    private void isValidMember(String email, String password) {
        Member member = memberRepository.findByEmail(email).orElseThrow(AuthenticationException::new);
        if (!member.getPassword().equals(password)) {
            throw new AuthenticationException();
        }
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> handleAuthenticationException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
