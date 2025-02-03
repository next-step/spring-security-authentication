package nextstep.app.domain;

import nextstep.security.AuthenticationException;
import nextstep.security.UserDetailService;
import nextstep.security.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailService {

    private final MemberRepository memberRepository;

    public CustomUserDetailService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserDetailsByUserName(String username) throws AuthenticationException {
        Member member = memberRepository.findByEmail(username)
                .orElseThrow(AuthenticationException::new);

        return CustomUserDetails.from(member);
    }
}
