package nextstep.app.application;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.security.UserDetails;
import nextstep.security.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class DefaultUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public DefaultUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 사용자를 찾을 수 없습니다."));

        return new DefaultUserDetails(member.getEmail(), member.getPassword());
    }
}
