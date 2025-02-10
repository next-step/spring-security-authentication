package nextstep.authentication;

import nextstep.authentication.exception.AuthenticationException;

public interface AuthenticationProvider {

    Authentication authenticate(Authentication authentication) throws AuthenticationException;

    boolean supports(Class<?> authentication);
}
