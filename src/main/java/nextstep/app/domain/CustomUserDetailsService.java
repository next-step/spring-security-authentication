package nextstep.app.domain;

import nextstep.security.UserDetails;
import nextstep.security.UserDetailsService;

public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public CustomUserDetailsService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) {
        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 사용자를 찾을 수 없습니다."));

        return new CustomMember(member);
    }
}
