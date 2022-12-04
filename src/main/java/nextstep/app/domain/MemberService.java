package nextstep.app.domain;

import nextstep.security.authentication.UserAuthenticationService;
import nextstep.security.exception.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService implements UserAuthenticationService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> getMembers() {
        return memberRepository.findAll();
    }

    public void validateMember(String email, String password) {
        Member member = memberRepository.findByEmail(email).orElseThrow(AuthenticationException::new);

        if (!member.getPassword().equals(password)) {
            throw new AuthenticationException();
        }
    }
}
