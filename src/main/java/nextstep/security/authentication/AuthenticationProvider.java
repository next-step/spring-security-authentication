package nextstep.security.authentication;

import nextstep.security.support.Authentication;

public interface AuthenticationProvider {
    Authentication doAuthentication(String email, String password);
}
