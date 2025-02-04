package nextstep.security.fixture;

import nextstep.security.Authentication;
import nextstep.security.AuthenticationException;
import nextstep.security.AuthenticationProvider;

public class TestAuthenticationProvider implements AuthenticationProvider {
    private boolean surpport;

    public TestAuthenticationProvider(boolean support) {
        this.surpport = surpport;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return new TestAuthentication(authentication.getPrincipal(), authentication.getPrincipal(), true);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return surpport;
    }
}
