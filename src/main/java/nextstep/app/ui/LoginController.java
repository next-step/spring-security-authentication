package nextstep.app.ui;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.app.infrastructure.InmemoryMemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class LoginController {
    private static final Member TEST_MEMBER = InmemoryMemberRepository.TEST_MEMBER_1;
    
    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";

    private final MemberRepository memberRepository;

    public LoginController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(HttpServletRequest request, HttpSession session) throws AuthenticationException {
        String name = request.getParameter("username");
        String password = request.getParameter("password");

        // 1. 사용자 정보 조회
        Member member = memberRepository.findByEmail(name).orElseThrow(AuthenticationException::new);

        // 2. 비밀번호 확인
        if (!password.equals(member.getPassword())) {
            // System.out.println("비밀번호 불일치"+password+member.getPassword());
            System.out.println("비밀번호 불일치");
            throw new AuthenticationException();
        }

        // 3. 세션에 인증 정보 저장
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, member);
        System.out.println("인증 정보 저장");

        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> handleAuthenticationException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
