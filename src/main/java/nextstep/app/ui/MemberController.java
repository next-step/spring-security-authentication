package nextstep.app.ui;

import nextstep.app.domain.MemberService;
import nextstep.app.domain.dto.MemberListResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MemberController extends BaseController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberListResponse>> list() {
        List<MemberListResponse> members = memberService.findAllMembers();
        return ResponseEntity.ok(members);
    }

}
