package nextstep.app;

import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.AuthenticationProvider;
import nextstep.security.authentication.exception.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class ProviderManager implements AuthenticationManager {

    private final List<AuthenticationProvider> authenticationProviders;

    public ProviderManager(List<AuthenticationProvider> authenticationProviders) {
        this.authenticationProviders = Objects.requireNonNull(authenticationProviders);
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
