package nextstep.security.interceptor;

import nextstep.app.domain.Member;
import nextstep.security.dto.MemberDTO;
import nextstep.app.domain.MemberRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

import static nextstep.security.SecurityConstants.SPRING_SECURITY_CONTEXT_KEY;

@Component
public class BasicAuthInterceptor implements HandlerInterceptor {

    private final MemberRepository memberRepository;

    public BasicAuthInterceptor(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("베이직 인터셉터");
        String authHeader = request.getHeader("Authorization");
        if (!(authHeader != null && authHeader.startsWith("Basic "))) { // invalid header
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        String base64Credentials = authHeader.substring("Basic ".length()).trim();
        String credentials = new String(Base64.getDecoder().decode(base64Credentials), StandardCharsets.UTF_8);
        if (!credentials.contains(":")) { // invalid header
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        final String[] values = credentials.split(":", 2);
        String username = values[0];
        String password = values[1];
        Optional<Member> memberOptional = memberRepository.findByEmail(username);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();

            // 1-2. 비밀번호 확인
            if (password.equals(member.getPassword())) {
                // 1-3. 세션에 인증 정보 저장
                HttpSession session = request.getSession();
                session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, member);
                return true;
            }
        }
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        return false; // 다음으로 요청을 진행
    }
}
