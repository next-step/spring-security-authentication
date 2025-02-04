package nextstep.app.config;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.security.exception.AuthenticationException;
import nextstep.security.domain.MemberDetail;
import nextstep.security.domain.MemberDetailService;
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
