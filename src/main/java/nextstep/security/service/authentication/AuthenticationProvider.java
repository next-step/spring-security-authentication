package nextstep.security.service.authentication;

import nextstep.app.ui.AuthenticationException;
import nextstep.security.model.authentication.Authentication;

public interface AuthenticationProvider {

    Authentication authenticate(Authentication authentication) throws AuthenticationException;

    boolean supports(Class<?> authentication);

}
