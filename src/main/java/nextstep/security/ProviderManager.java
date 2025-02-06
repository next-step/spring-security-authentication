package nextstep.security;

import nextstep.security.exception.AuthenticationException;

import java.util.List;

public class ProviderManager implements AuthenticationManager {

    List<AuthenticationProvider> providers;

    public ProviderManager(List<AuthenticationProvider> providers) {
        this.providers = providers;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        return providers.stream()
                .filter(provider -> provider.supports(authentication.getClass()))
                .map(provider -> provider.authenticate(authentication))
                .findFirst()
                .orElseThrow(AuthenticationException::new);
    }
}
