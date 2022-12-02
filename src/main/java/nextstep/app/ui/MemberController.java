package nextstep.app.ui;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    public static final String AUTHORIZATION = "Basic";
    public static final String SPLIT_CHAR = ":";

    @GetMapping("/members")
    public ResponseEntity<List<Member>> list(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        if(!authorization.startsWith(AUTHORIZATION)) {
            throw new AuthenticationException();
        }
        String decodeTarget = authorization.substring(AUTHORIZATION.length()).trim();
        String decodeValue = new String(Base64Utils.decode(decodeTarget.getBytes()));

        String[] emailAndPassword = decodeValue.split(SPLIT_CHAR);
        String email = emailAndPassword[0];
        String password = emailAndPassword[1];

        memberService.validateMember(email, password);

        List<Member> members = memberService.getMembers();
        return ResponseEntity.ok(members);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> handleAuthenticationException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}