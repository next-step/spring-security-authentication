package nextstep.app.ui;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class MemberController {

    private final MemberRepository memberRepository;

    public MemberController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @GetMapping("/members")
    public ResponseEntity<List<Member>> list(HttpServletRequest request) {

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
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            List<Member> members = memberRepository.findAll();
            return ResponseEntity.ok(members);
        } catch (Exception e) {
            throw new AuthenticationException();
        }
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> handleAuthenticationException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
