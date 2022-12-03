package nextstep.app.ui;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.security.authentication.BasicAuthentication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class MemberController {

    private static final String CREDENTIALS = "aHR0cCBiYXNpYw==";
    private final MemberRepository memberRepository;

    public MemberController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @GetMapping("/members")
    public ResponseEntity<List<Member>> list(final HttpServletRequest request) {
        List<Member> members = memberRepository.findAll();

        final var authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        final var authentication = new BasicAuthentication(CREDENTIALS, authorization);
        request.setAttribute("authentication", authentication);

        return ResponseEntity.ok(members);
    }
}
