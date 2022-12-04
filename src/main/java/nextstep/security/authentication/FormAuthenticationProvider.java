package nextstep.security.authentication;

import nextstep.security.support.Authentication;
import nextstep.security.support.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class FormAuthenticationProvider implements AuthenticationProvider {

    public Authentication doAuthentication(String email, String password) {
        Authentication authentication = new FormAuthenticationToken(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }
}
