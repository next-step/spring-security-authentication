package nextstep.app.ui;

import nextstep.app.domain.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class LoginController extends BaseController {
    private final MemberService memberService;

    public LoginController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(HttpServletRequest request, HttpSession session) {
        String email = request.getParameter("username");
        String password = request.getParameter("password");
        memberService.login(session, email, password);
        return ResponseEntity.ok().build();
    }
}
