package nextstep.authentication;

import nextstep.authentication.exception.AuthenticationException;

public interface AuthenticationManager {

    Authentication authenticate(Authentication authentication) throws AuthenticationException;
}
