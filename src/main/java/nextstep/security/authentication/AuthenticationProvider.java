package nextstep.security.authentication;

import nextstep.app.ui.AuthenticationException;

public interface AuthenticationProvider {
    Authentication authenticate(final Authentication authentication) throws AuthenticationException;

    boolean supports(final Class<?> authentication);
}
