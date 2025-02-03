package nextstep.security.authentication;

import nextstep.security.authentication.exception.AuthenticationException;

public interface AuthenticationProvider {
    Authentication authenticate(Authentication authentication) throws AuthenticationException;

    boolean supports(Class<?> authentication);
}