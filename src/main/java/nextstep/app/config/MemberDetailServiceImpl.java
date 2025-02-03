package nextstep.app.config;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.security.AuthenticationException;
import nextstep.security.MemberDetail;
import nextstep.security.MemberDetailService;
import org.springframework.stereotype.Service;

@Service
public class MemberDetailServiceImpl implements MemberDetailService {

    private final MemberRepository memberRepository;

    public MemberDetailServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    @Override
    public MemberDetail findByUsername(String username) {
        Member member = memberRepository.findByEmail(username)
                .orElseThrow(AuthenticationException::new);

        return new MemberDetail(member.getEmail(), member.getPassword());
    }
}
