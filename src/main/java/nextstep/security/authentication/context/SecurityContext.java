package nextstep.security.authentication.context;

import nextstep.security.authentication.Authentication;

public interface SecurityContext {
    Authentication getAuthentication();

    void setAuthentication(Authentication authentication);
}
