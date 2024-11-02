package nextstep.app.domain;

import nextstep.app.exception.AuthErrorCodes;
import nextstep.app.exception.AuthenticationException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class MemberService {
    private MemberRepository memberRepo;


    public MemberService(MemberRepository memberRepo) {
        this.memberRepo = memberRepo;
    }

    public void login(HttpSession session, String email, String password) {
        memberRepo.findByEmail(email).ifPresentOrElse(

                member -> {
                    if (member.getPassword().equals(password)) {
                        session.setAttribute("SPRING_SECURITY_CONTEXT", member);
                        return;
                    }
                    throw new AuthenticationException(AuthErrorCodes.UNAUTHORIZED_LOGIN_REQUEST);
                },

                () -> {
                    throw new AuthenticationException(AuthErrorCodes.UNAUTHORIZED_LOGIN_REQUEST);
                }
        );

    }
}
