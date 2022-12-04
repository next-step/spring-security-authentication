package nextstep.app.applicaion;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.security.exception.AuthenticationException;
import nextstep.security.userdetils.User;
import nextstep.security.userdetils.UserDetails;
import nextstep.security.userdetils.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        final Member member = memberRepository.findByEmail(username)
            .orElseThrow(AuthenticationException::new);
        return new User(member.getEmail(), member.getPassword());
    }
}
