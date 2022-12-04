package nextstep.security.authentication.provider;

import nextstep.security.authentication.Authentication;
import nextstep.security.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationProvider {

    public Authentication authenticate(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }
}
