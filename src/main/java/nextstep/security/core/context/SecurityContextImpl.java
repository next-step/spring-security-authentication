package nextstep.security.core.context;

import nextstep.security.authentication.Authentication;

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
