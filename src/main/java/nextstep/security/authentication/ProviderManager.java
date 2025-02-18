package nextstep.security.authentication;

import java.util.List;

public class ProviderManager implements AuthenticationManager {

    private final List<AuthenticationProvider> providers;

    public ProviderManager(final List<AuthenticationProvider> providers) {
        this.providers = providers;
    }

    @Override
    public Authentication authenticate(final Authentication authentication) {
        return this.providers.stream()
                .filter(provider -> provider.supports(authentication.getClass()))
                .findFirst()
                .map(provider -> provider.authenticate(authentication))
                .orElse(null);
    }
}
