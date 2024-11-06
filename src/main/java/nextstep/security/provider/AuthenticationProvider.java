package nextstep.security.provider;

import nextstep.security.model.SecurityAuthentication;

public interface AuthenticationProvider {
    SecurityAuthentication authenticate(SecurityAuthentication authentication);

    boolean supports(Class<?> authentication);
}
