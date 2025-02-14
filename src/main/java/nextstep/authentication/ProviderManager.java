package nextstep.authentication;

import nextstep.authentication.exception.AuthenticationException;
import nextstep.authentication.exception.ProviderNotFoundException;

import java.util.List;
import java.util.Objects;

public class ProviderManager implements AuthenticationManager {

    private final List<AuthenticationProvider> providers;

    public ProviderManager(List<AuthenticationProvider> providers) {
        this.providers = providers;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        Class<? extends Authentication> target = authentication.getClass();

        return providers.stream()
                .filter(provider -> provider.supports(target))
                .map(provider -> provider.authenticate(authentication))
                .filter(Objects::nonNull)
                .findAny()
                .orElseThrow(() -> new ProviderNotFoundException(String.format("No AuthenticationProvider found for %s", target.getName())));
    }
}
