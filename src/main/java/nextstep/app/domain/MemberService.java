package nextstep.app.domain;

import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationToken;
import nextstep.security.authentication.provider.AuthenticationProvider;
import nextstep.security.exception.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthenticationProvider authenticationProvider;

    public MemberService(MemberRepository memberRepository, AuthenticationProvider authenticationProvider) {
        this.memberRepository = memberRepository;
        this.authenticationProvider = authenticationProvider;
    }

    public List<Member> getMembers() {
        return memberRepository.findAll();
    }

    public void authenticateMemberInfo(String email, String password) {
        this.isValidMemberInfo(email, password);

        Authentication authentication = new AuthenticationToken(email, password);
        authenticationProvider.authenticate(authentication);
    }

    private void isValidMemberInfo(String email, String password) {
        Member member = memberRepository
                .findByEmail(email)
                .orElseThrow(AuthenticationException::new);

        if (!member.getPassword().equals(password)) {
            throw new AuthenticationException();
        }
    }
}
