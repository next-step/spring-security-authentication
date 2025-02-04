package nextstep.app.domain;

import nextstep.app.ui.AuthenticationException;
import nextstep.security.UserDetail;
import nextstep.security.UserDetailService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailService {

    private final MemberRepository memberRepository;

    public UserDetailServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetail loadUserByUsername(String username) {
        Member member = memberRepository.findByEmail(username)
                .orElseThrow(AuthenticationException::new);

        return new UserDetailImpl(member);
    }
}
