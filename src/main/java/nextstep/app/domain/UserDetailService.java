package nextstep.app.domain;

import org.springframework.stereotype.Service;

@Service
public class UserDetailService {

    private final MemberRepository memberRepository;

    public UserDetailService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member retrieveMemberByEmailAndPassword(String email, String password) {
        return memberRepository.findByEmail(email)
                .filter(member -> member.getPassword().equals(password))
                .orElse(null);
    }
}
