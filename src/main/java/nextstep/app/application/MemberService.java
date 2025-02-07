package nextstep.app.application;

import nextstep.app.domain.MemberRepository;
import nextstep.app.payload.MemberDetails;
import nextstep.security.exceptions.AuthenticationException;
import nextstep.security.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    
    @Override
    public MemberDetails findUserDetailsByPrincipal(final Object principal) {
        var member = memberRepository.findByEmail((String) principal)
                .orElseThrow(AuthenticationException::new);
        return MemberDetails.from(member);
    }

}
