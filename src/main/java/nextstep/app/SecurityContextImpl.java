package nextstep.app;

import nextstep.security.authentication.Authentication;
import nextstep.security.SecurityContext;

public class SecurityContextImpl implements SecurityContext {
    private Authentication authentication;

    @Override
    public Authentication getAuthentication() {
        return authentication;
    }

    @Override
    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }
}
