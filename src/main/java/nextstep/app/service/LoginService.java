package nextstep.app.service;

import lombok.RequiredArgsConstructor;
import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.app.ui.AuthenticationException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

import static nextstep.app.ui.LoginController.SPRING_SECURITY_CONTEXT_KEY;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    public Member login(String email, String password) {
        //로그인 실패 시 예외 발생
        Member member = memberRepository.findByEmail(email).orElseThrow(AuthenticationException::new);
        if (!email.equals(member.getEmail()) || !password.equals(member.getPassword())) {
            throw new AuthenticationException();
        }
        return member;
    }
}
