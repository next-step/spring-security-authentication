package nextstep.security.provider;

import nextstep.security.authentication.Authentication;

public interface AuthenticationProvider {

    boolean supports(Class<?> authentication);

    Authentication authenticate(Authentication authentication);
}
