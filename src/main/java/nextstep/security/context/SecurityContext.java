package nextstep.security.context;

import nextstep.security.authentication.Authentication;

public class SecurityContext {
    private Authentication authentication;

    public SecurityContext() {
    }

    public SecurityContext(final Authentication authentication) {
        this.authentication = authentication;
    }

    public void setAuthentication(final Authentication authentication) {
        this.authentication = authentication;
    }

    public Authentication getAuthentication() {
        return authentication;
    }
}
