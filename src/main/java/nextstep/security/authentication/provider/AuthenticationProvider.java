package nextstep.security.authentication.provider;

import nextstep.security.authentication.Authentication;

public interface AuthenticationProvider {

    Authentication authenticate(Authentication authentication);

    boolean supports(Class<?> authentication);
}
