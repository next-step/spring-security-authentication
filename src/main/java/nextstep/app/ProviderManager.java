package nextstep.app;

import nextstep.security.Authentication;
import nextstep.security.AuthenticationManager;
import nextstep.security.AuthenticationProvider;
import nextstep.security.exception.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProviderManager implements AuthenticationManager {

    private final List<AuthenticationProvider> authenticationProviders;

    public ProviderManager(List<AuthenticationProvider> authenticationProviders) {
        this.authenticationProviders = authenticationProviders;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        AuthenticationProvider provider = authenticationProviders.stream()
                .filter(it -> it.supports(authentication.getClass()))
                .findAny()
                .orElseThrow(AuthenticationException::new);

        return provider.authenticate(authentication);
    }
}
