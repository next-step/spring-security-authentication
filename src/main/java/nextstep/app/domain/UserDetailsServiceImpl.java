package nextstep.app.domain;

import nextstep.security.domain.UserDetails;
import nextstep.security.domain.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;

    public UserDetailsServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsernameAndPassword(String username, String password) {
        return memberRepository.findByEmail(username)
                .map(UserDetailsImpl::of)
                .filter(userDetails -> userDetails.verifyPassword(password))
                .orElse(UserDetailsImpl.empty());
    }
}
