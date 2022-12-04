package nextstep.app.ui;

import nextstep.app.domain.MemberService;
import nextstep.security.authentication.FormAuthenticationProvider;
import nextstep.security.support.SecurityContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
public class LoginController {
    private final MemberService memberService;

    private final FormAuthenticationProvider formAuthenticationProvider;

    public LoginController(MemberService memberService, FormAuthenticationProvider formAuthenticationProvider) {
        this.memberService = memberService;
        this.formAuthenticationProvider = formAuthenticationProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());

        Map<String, String[]> paramMap = request.getParameterMap();
        String email = paramMap.get("username")[0];
        String password = paramMap.get("password")[0];

        memberService.validateMember(email, password);

        formAuthenticationProvider.doAuthentication(email, password);

        return ResponseEntity.ok().build();
    }
}
