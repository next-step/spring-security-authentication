package nextstep.security.authentication;

import nextstep.security.support.Authentication;
import nextstep.security.support.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationProvider {

    private final UserAuthenticationService userAuthenticationService;

    public AuthenticationProvider(UserAuthenticationService userAuthenticationService) {
        this.userAuthenticationService = userAuthenticationService;
    }

    public void clearAuthentication() {
        SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());
    }

    public Authentication doAuthentication(Authentication authentication) {
        userAuthenticationService.validateMember(
                authentication.getPrincipal().toString(),
                authentication.getCredentials().toString()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }
}
