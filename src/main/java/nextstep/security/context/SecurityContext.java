package nextstep.security.context;

import nextstep.security.Authentication;

public interface SecurityContext {

    Authentication getAuthentication();

    void setAuthentication(Authentication authentication);
}
