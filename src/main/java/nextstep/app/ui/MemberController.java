package nextstep.app.ui;

import jakarta.servlet.http.HttpSession;
import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.security.context.SecurityContext;
import nextstep.security.exceptions.AuthorizationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static nextstep.security.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@RestController
public class MemberController {

    private final MemberRepository memberRepository;

    public MemberController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @GetMapping("/members")
    public ResponseEntity<List<Member>> list(
            HttpSession httpSession) {
        var session = (SecurityContext) httpSession.getAttribute(SPRING_SECURITY_CONTEXT_KEY);
        if (session == null) {
            throw new AuthorizationException();
        }

        return ResponseEntity.ok(memberRepository.findAll());
    }
}
