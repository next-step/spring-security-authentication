package nextstep.security.fixture;

import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationException;
import nextstep.security.authentication.AuthenticationProvider;

public class TestAuthenticationProvider implements AuthenticationProvider {
    private boolean support;

    public TestAuthenticationProvider(boolean support) {
        this.support = support;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return new TestAuthentication(authentication.getPrincipal(), authentication.getPrincipal(), true);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return support;
    }
}
