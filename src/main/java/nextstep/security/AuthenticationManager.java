package nextstep.security;

import nextstep.security.exception.AuthenticationException;

public interface AuthenticationManager {

    Authentication authenticate(Authentication authentication) throws AuthenticationException;
}
