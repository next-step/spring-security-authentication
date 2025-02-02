package nextstep.app.auth;

import nextstep.app.domain.MemberRepository;
import nextstep.security.exception.ForbiddenException;
import nextstep.security.user.UserDetails;
import nextstep.security.user.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class MemberUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public MemberUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return new MemberUserDetails(
                memberRepository.findByEmail(username).orElseThrow(
                        () -> new ForbiddenException("User Not Found")
                )
        );
    }
}
