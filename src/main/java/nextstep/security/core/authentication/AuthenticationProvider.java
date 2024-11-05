package nextstep.security.core.authentication;

import nextstep.security.core.SecurityPrincipal;
import nextstep.security.exception.AuthenticationException;

public interface AuthenticationProvider {
    Authentication authenticate(Authentication authentication) throws AuthenticationException;

    boolean supports(SecurityPrincipal securityPrincipal);

}
