package nextstep.security.manager;

import nextstep.security.authentication.Authentication;

public interface AuthenticationManager {

    Authentication authenticate(Authentication authentication);
}
