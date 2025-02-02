package nextstep.security.authentication.manager;

import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.provider.AuthenticationProvider;
import nextstep.security.exception.AuthenticationException;

import java.util.List;

public class ProviderManager implements AuthenticationManager {
    private final List<AuthenticationProvider> providers;

    public ProviderManager(List<AuthenticationProvider> providers) {
        this.providers = providers;
    }

    @Override
    public Authentication authenticate(Authentication authenticationToken) {
        return providers.stream().filter(
                provider -> provider.supports(authenticationToken.getClass())
        ).findFirst().orElseThrow(
                AuthenticationException::notSupported
        ).authenticate(authenticationToken);
    }
}
