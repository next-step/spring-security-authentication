package nextstep.app.domain;

import nextstep.security.core.uesrdetails.UserDetails;
import nextstep.security.core.uesrdetails.UserDetailsService;
import nextstep.security.exception.AuthenticationException;
import nextstep.security.role.Role;

public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public CustomUserDetailsService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) {
        final CustomMember customMember = fetchUser(username);

        return customMember;
    }

    private CustomMember fetchUser(final String username) {
        final CustomMember customMember = memberRepository.findByEmail(username)
                .map(CustomMember::new)
                .orElseThrow(() -> new AuthenticationException());

        customMember.addAuthority(new Role("NORMAL"));
        return customMember;
    }
}
