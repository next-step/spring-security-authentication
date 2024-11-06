package nextstep.security.provider;

import nextstep.security.model.SecurityAuthentication;

public interface AuthenticationManager {
    SecurityAuthentication authenticate(SecurityAuthentication authentication);
}
