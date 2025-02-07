package nextstep.security.authentication;

import java.util.List;

public class ProviderManager implements AuthenticationManager {

    private final List<AuthenticationProvider> authenticationProvider;

    public ProviderManager(final List<AuthenticationProvider> authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    public Authentication authenticate(final Authentication authentication) {
        return authenticationProvider.stream()
                .filter(it -> it.supports(authentication.getClass()))
                .findFirst()
                .orElseThrow()
                .authenticate(authentication);
    }

}
