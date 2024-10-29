package nextstep.app.service;

import nextstep.app.domain.MemberRepository;
import nextstep.app.ui.AuthenticationException;
import nextstep.security.service.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";

    private final MemberRepository memberRepository;
    private final HttpSession session;

    public UserDetailsServiceImpl(MemberRepository memberRepository,
                                  HttpSession httpSession) {
        this.memberRepository = memberRepository;
        this.session = httpSession;
    }

    @Override
    public void authenticate(String username, String password) {
        memberRepository.findByEmail(username)
                .filter(member -> member.getPassword().equals(password))
                .ifPresentOrElse(
                        member -> session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, member),
                        () -> { throw new AuthenticationException(); }
                );
    }
}
