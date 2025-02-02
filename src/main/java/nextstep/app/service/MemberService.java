package nextstep.app.service;

import nextstep.app.domain.MemberRepository;
import nextstep.security.service.MemberValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemberService implements MemberValidationService {
    private final MemberRepository memberRepository;
    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    @Override
    public boolean isValidMember(String email, String password) {
        return memberRepository.findByEmail(email).isPresent();
    }
}
