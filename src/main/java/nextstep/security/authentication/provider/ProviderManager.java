package nextstep.security.authentication.provider;

import nextstep.security.authentication.Authentication;
import nextstep.security.exception.AuthenticationException;

import java.util.List;

public class ProviderManager implements AuthenticationManager {

    private final List<AuthenticationProvider> authenticationProviders;

    public ProviderManager(final List<AuthenticationProvider> authenticationProviders) {
        this.authenticationProviders = authenticationProviders;
    }

    @Override
    public Authentication authentication(Authentication authentication) {
        return authenticationProviders.stream()
                .filter(provider -> provider.supports(authentication.getClass()))
                .findFirst()
                .map(provider -> provider.authenticate(authentication))
                .orElseThrow(() -> new AuthenticationException("인증할 수 없습니다."));
    }
}
