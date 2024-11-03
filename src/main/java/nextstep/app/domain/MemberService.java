package nextstep.app.domain;

import nextstep.app.exception.AuthErrorCodes;
import nextstep.app.exception.AuthenticationException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Base64;

@Service
public class MemberService {
    private MemberRepository memberRepo;


    public MemberService(MemberRepository memberRepo) {
        this.memberRepo = memberRepo;
    }

    public void login(HttpSession session, String email, String password) {
        memberRepo.findByEmail(email).ifPresentOrElse(member -> {
            if (member.getPassword().equals(password)) {
                session.setAttribute("SPRING_SECURITY_CONTEXT", member);
                return;
            }
            throw new AuthenticationException(AuthErrorCodes.UNAUTHORIZED_LOGIN_REQUEST);
        }, () -> {
            throw new AuthenticationException(AuthErrorCodes.UNAUTHORIZED_LOGIN_REQUEST);
        });
    }

    public void validate(String basicToken){
        String decoded = new String(Base64.getDecoder().decode(basicToken.replace("Basic ", "")));
        String email = "";
        String password = "";
        try {
            email = decoded.substring(0, decoded.indexOf(":"));
            password = decoded.substring(decoded.indexOf(":") + 1);
        }
        catch (StringIndexOutOfBoundsException e){
            throw new AuthenticationException(AuthErrorCodes.WRONG_BASIC_TOKEN_FORMAT);
        }

        final String finalPassword = password;
        memberRepo.findByEmail(email).ifPresentOrElse(member -> {
            if (member.getPassword().equals(finalPassword)) {
                return;
            }
            throw new AuthenticationException(AuthErrorCodes.UNAUTHORIZED_LOGIN_REQUEST);
        }, () -> {
            throw new AuthenticationException(AuthErrorCodes.UNAUTHORIZED_LOGIN_REQUEST);
        });
    }
}
