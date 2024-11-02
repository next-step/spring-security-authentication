package nextstep.app.interceptor;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.app.ui.AuthenticationException;
import org.springframework.util.Base64Utils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;

public class BasicAuthInterceptor implements HandlerInterceptor {
    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";
    private final MemberRepository memberRepository;

    public BasicAuthInterceptor(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String basicToken = request.getHeader("Authorization");
        HttpSession session = request.getSession();

        try {
            authenticate(basicToken, session);
        } catch (AuthenticationException e) {
            response.setStatus(401);
            return false;
        }

        return true;
    }


    private void authenticate(String token, HttpSession session) throws AuthenticationException {
        String payload = token.split(" ")[1];

        String decodedPayload = new String(Base64Utils.decodeFromString(payload), StandardCharsets.UTF_8);
        String[] memberInfo = decodedPayload.split(":");

        if (memberInfo.length != 2) {
            throw new AuthenticationException();
        }

        Member validMember = isValidMember(memberInfo[0], memberInfo[1]);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, validMember);
    }

    private Member isValidMember(String email, String password) {
        Member member = memberRepository.findByEmail(email).orElseThrow(AuthenticationException::new);
        if (!member.getPassword().equals(password)) {
            throw new AuthenticationException();
        }
        return member;
    }
}
