package nextstep.app.application;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.security.userdetail.UserDetail;
import nextstep.security.userdetail.UserDetailService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailService {

    private final MemberRepository memberRepository;

    public UserDetailServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetail getUserDetail(String username) {
        return memberRepository.findByEmail(username)
                .map(this::convertToUserDetail)
                .orElse(null);
    }

    public UserDetail convertToUserDetail(Member member) {
        return new UserDetail(member.getEmail(), member.getPassword());
    }
}
