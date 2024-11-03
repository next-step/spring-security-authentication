package nextstep.app.ui;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static nextstep.app.ui.LoginController.SPRING_SECURITY_CONTEXT_KEY;

@RestController
public class MemberController {

    private final MemberRepository memberRepository;

    public MemberController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @GetMapping("/members")
    public ResponseEntity<List<Member>> list(HttpServletRequest request, HttpSession session) {
        // 1. 권한 확인 - Member로 등록된 사용자인지 확인 및 Session 저장
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Basic ")) {
            String base64Credentials = authHeader.substring("Basic ".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials), StandardCharsets.UTF_8);
            if (!credentials.contains(":")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            final String[] values = credentials.split(":", 2);
            String username = values[0];
            String password = values[1];

            // 1-1. 사용자 정보 조회
            Optional<Member> memberOptional = memberRepository.findByEmail(username);
            if (memberOptional.isPresent()) {
                Member member = memberOptional.get();

                // 1-2. 비밀번호 확인
                if (password.equals(member.getPassword())) {
                    // 1-3. 세션에 인증 정보 저장
                    session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, member);
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 2. Member 목록 조회
        List<Member> members = memberRepository.findAll();
        return ResponseEntity.ok(members);
    }

}
