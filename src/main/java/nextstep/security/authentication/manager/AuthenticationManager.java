package nextstep.security.authentication.manager;

import nextstep.security.authentication.Authentication;

public interface AuthenticationManager {
    Authentication authenticate(Authentication authenticationToken);
}
