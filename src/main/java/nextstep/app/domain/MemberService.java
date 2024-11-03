package nextstep.app.domain;

import nextstep.app.exception.AuthErrorCodes;
import nextstep.app.exception.AuthenticationException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Base64;
import java.util.Optional;

@Service
public class MemberService {

    private static String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";
    private MemberRepository memberRepo;


    public MemberService(MemberRepository memberRepo) {
        this.memberRepo = memberRepo;
    }

    public void login(HttpSession session, String email, String password) {
        Member member = findUserByCredential(email, password);
        session.setAttribute(SPRING_SECURITY_CONTEXT, member);
    }

    public void validate(String basicToken){
        String decoded = new String(Base64.getDecoder().decode(basicToken.replace("Basic ", "")));
        String email = "";
        String password = "";
        try {
            email = decoded.substring(0, decoded.indexOf(":"));
            password = decoded.substring(decoded.indexOf(":") + 1);
            findUserByCredential(email, password);
        }
        catch (StringIndexOutOfBoundsException e){
            throw new AuthenticationException(AuthErrorCodes.WRONG_BASIC_TOKEN_FORMAT);
        }
    }
    private Member findUserByCredential(String email, String pw) {
        return memberRepo.findByEmail(email)
                .filter(user -> user.getPassword().equals(pw))
                .orElseThrow(() -> new AuthenticationException(AuthErrorCodes.UNAUTHORIZED_LOGIN_REQUEST));
    }
}
