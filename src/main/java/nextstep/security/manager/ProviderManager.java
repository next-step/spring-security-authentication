package nextstep.security.manager;

import nextstep.security.authentication.Authentication;
import nextstep.security.provider.AuthenticationProvider;

import java.util.List;

public class ProviderManager implements AuthenticationManager {

    private final List<AuthenticationProvider> providers;

    public ProviderManager(List<AuthenticationProvider> providers) {
        this.providers = providers;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        return providers.stream()
                .filter(v -> v.supports(authentication.getClass()))
                .map(v -> v.authenticate(authentication))
                .findFirst()
                .orElse(null);
    }
}
