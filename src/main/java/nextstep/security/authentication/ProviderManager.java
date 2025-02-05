package nextstep.security.authentication;

import java.util.List;

public class ProviderManager implements AuthenticationManager {
    private List<AuthenticationProvider> providers;

    public ProviderManager(List<AuthenticationProvider> providers) {
        this.providers = providers;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        for (AuthenticationProvider provider : providers) {
            if (!provider.supports(authentication.getClass())) {
                continue;
            }
            return provider.authenticate(authentication);
        }
        return authentication;
    }
}
