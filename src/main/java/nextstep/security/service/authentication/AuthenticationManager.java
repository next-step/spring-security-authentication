package nextstep.security.service.authentication;

import nextstep.security.model.authentication.Authentication;

public interface AuthenticationManager {

    Authentication authenticate(Authentication authentication);

}
