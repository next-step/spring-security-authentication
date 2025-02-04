package nextstep.app.ui;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.security.Authentication;
import nextstep.security.SecurityContext;
import nextstep.security.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MemberController {

    private final MemberRepository memberRepository;

    public MemberController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @GetMapping("/members")
    public ResponseEntity<List<Member>> list() {
        if (isNotAuthenticated()) {
            return new ResponseEntity<>(List.of(), HttpStatus.UNAUTHORIZED);
        }

        List<Member> members = memberRepository.findAll();
        return ResponseEntity.ok(members);
    }

    private boolean isNotAuthenticated() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication authentication = ctx.getAuthentication();

        if (authentication == null) {
            return true;
        }

        return !authentication.isAuthenticated();
    }
}
