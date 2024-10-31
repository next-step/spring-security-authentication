package nextstep.app.domain;

import nextstep.security.service.UserDetailsService;
import nextstep.security.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByEmailAndPassword(String email, String password) {
        return memberRepository.findByEmail(email)
                .filter(v -> v.getPassword().equals(password))
                .orElse(null);
    }
}
