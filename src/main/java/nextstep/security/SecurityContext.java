package nextstep.security;

import nextstep.security.authentication.Authentication;

public interface SecurityContext {
    Authentication getAuthentication();
    void setAuthentication(Authentication authentication);
}
