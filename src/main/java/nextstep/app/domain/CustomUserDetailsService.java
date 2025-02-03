package nextstep.app.domain;

import nextstep.security.authentication.exception.AuthenticationException;
import nextstep.security.user.UserDetailsService;
import nextstep.security.user.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserDetailsByUserName(String username) throws AuthenticationException {
        Member member = memberRepository.findByEmail(username)
                .orElseThrow(AuthenticationException::new);

        return CustomUserDetails.from(member);
    }
}
