package nextstep.app.domain;

import nextstep.security.exception.AuthenticationException;
import nextstep.security.user.UserDetails;
import nextstep.security.user.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    public UserDetailServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new AuthenticationException("사용자를 찾을 수 없습니다."));

        return new UserDetail(member.getName(), member.getPassword());
    }
}
