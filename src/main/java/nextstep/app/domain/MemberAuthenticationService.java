package nextstep.app.domain;

import nextstep.security.authentication.UserAuthenticationService;
import nextstep.security.exception.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class MemberAuthenticationService implements UserAuthenticationService {
    private final MemberRepository memberRepository;

    public MemberAuthenticationService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void validateMember(String email, String password) {
        Member member = memberRepository.findByEmail(email).orElseThrow(AuthenticationException::new);

        if (!member.getPassword().equals(password)) {
            throw new AuthenticationException();
        }
    }
}
