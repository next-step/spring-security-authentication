package nextstep.app.service;

import nextstep.app.domain.MemberRepository;
import nextstep.app.ui.AuthenticationException;
import nextstep.security.model.UserDetail;
import nextstep.security.service.UserDetailService;
import org.springframework.stereotype.Service;

@Service
public class MemberService implements UserDetailService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public UserDetail isValidUser(String email, String password) {
        UserDetail userDetail = memberRepository.findByEmail(email).orElseThrow(AuthenticationException::new);
        if (!userDetail.getPassword().equals(password)) {
            throw new AuthenticationException();
        }
        return userDetail;
    }
}
