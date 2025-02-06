package nextstep.app.domain;

import nextstep.security.core.uesrdetails.UserDetails;
import nextstep.security.core.uesrdetails.UserDetailsService;
import nextstep.security.exception.AuthenticationException;

public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public CustomUserDetailsService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) {
        return memberRepository.findByEmail(username)
                .map(CustomMember::new)
                .orElseThrow(() -> new AuthenticationException());
    }
}
