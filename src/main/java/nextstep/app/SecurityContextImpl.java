package nextstep.app;

import nextstep.security.authentication.Authentication;
import nextstep.security.SecurityContext;

public class SecurityContextImpl implements SecurityContext {
    private Authentication authentication;

    private SecurityContextImpl() {

    }

    public SecurityContextImpl(Authentication authentication) {
        this.authentication = authentication;
    }

    public static SecurityContext empty() {
        return new SecurityContextImpl();
    }

    @Override
    public Authentication getAuthentication() {
        return authentication;
    }

    @Override
    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }
}
