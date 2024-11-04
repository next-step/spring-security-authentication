package nextstep.app.service;

import nextstep.app.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService {

    private final MemberRepository memberRepository;

    public UserDetailService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public UserDetail findUserDetail(String email) {
        return memberRepository.findByEmail(email)
                .map(member -> new UserDetail(member.getEmail(), member.getPassword()))
                .orElse(null);
    }

}
