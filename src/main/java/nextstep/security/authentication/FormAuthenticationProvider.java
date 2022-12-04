package nextstep.security.authentication;

import nextstep.security.support.Authentication;
import nextstep.security.support.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class FormAuthenticationProvider implements AuthenticationProvider {
    public Authentication doAuthentication(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }
}
