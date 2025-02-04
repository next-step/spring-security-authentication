package nextstep.security;

import java.util.Optional;
import java.util.Set;

public class ProviderManager implements AuthenticationManager {
    private final Set<AuthenticationProvider> providers;

    public ProviderManager(Set<AuthenticationProvider> providers) {
        this.providers = providers;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        return findSupportProvider(authentication)
                .map((provider) -> provider.authenticate(authentication))
                .orElse(authentication);
    }

    private Optional<AuthenticationProvider> findSupportProvider(Authentication authentication) {
        return providers.stream()
                .filter((it) -> it.supports(authentication.getClass()))
                .findFirst();
    }
}
