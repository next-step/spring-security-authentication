package nextstep.security.authentication;

import nextstep.security.exception.AuthenticationException;

import java.util.List;

public class ProviderManager implements AuthenticationManager {
    private final List<AuthenticationProvider> providers;

    public ProviderManager(final List<AuthenticationProvider> providers) {
        this.providers = providers;
    }

    @Override
    public Authentication authenticate(final Authentication authentication) {
        return providers.stream()
                .filter(provider -> provider.supports(authentication.getClass()))
                .findFirst()
                .map(provider -> provider.authenticate(authentication))
                .orElseThrow(AuthenticationException::new);
    }
}
