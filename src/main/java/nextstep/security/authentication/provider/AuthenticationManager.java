package nextstep.security.authentication.provider;

import nextstep.security.authentication.Authentication;

@FunctionalInterface
public interface AuthenticationManager {
    Authentication authentication(Authentication authentication);
}
