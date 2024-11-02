package nextstep.app.domain.member.service;

import nextstep.app.domain.member.param.Member;
import nextstep.app.domain.member.repository.MemberRepository;
import nextstep.security.service.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Member retrieveMemberByEmailAndPassword(String email, String password) {
        return memberRepository.findByEmail(email)
                .filter(member -> member.getPassword().equals(password))
                .orElse(null);
    }
}
