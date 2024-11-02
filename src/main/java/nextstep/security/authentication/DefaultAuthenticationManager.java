package nextstep.security.authentication;

import java.util.List;

public class DefaultAuthenticationManager implements AuthenticationManager {

    private final List<AuthenticationProvider> authenticationProviders;

    public DefaultAuthenticationManager(List<AuthenticationProvider> authenticationProviders) {
        this.authenticationProviders = authenticationProviders;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        for (AuthenticationProvider provider : authenticationProviders) {

            if (!provider.supports(authentication.getClass())) {
                continue;
            }

            return provider.authenticate(authentication);
        }

        return null;
    }

}
