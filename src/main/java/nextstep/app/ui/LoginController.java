package nextstep.app.ui;

import nextstep.app.domain.LoginService;
import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
public class LoginController {
    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";

    private final MemberRepository memberRepository;
    private final LoginService loginService;

    public LoginController(MemberRepository memberRepository, LoginService loginService) {
        this.memberRepository = memberRepository;
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

//        try {
//            Member loginMember = loginService.login(request.getParameter("username"), request.getParameter("password"));
//
//            if (loginMember == null) {
//                System.out.println("로그인에 실패하였습니다.");
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//            }
////            로그인 성공 처리
////            세션에 로그인 회원 정보 보관
//            session.setAttribute("SPRING_SECURITY_CONTEXT", loginMember);
//            } catch (Exception e) {
//                throw new AuthenticationException();
//            }

        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> handleAuthenticationException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
