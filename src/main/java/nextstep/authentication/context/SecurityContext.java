package nextstep.authentication.context;

import nextstep.authentication.Authentication;

public interface SecurityContext {

    Authentication getAuthentication();

    void setAuthentication(Authentication authentication);
}
