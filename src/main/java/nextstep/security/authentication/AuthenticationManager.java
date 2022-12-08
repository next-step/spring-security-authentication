package nextstep.security.authentication;

import nextstep.security.exception.AuthenticationException;

import java.util.List;

public class AuthenticationManager {

    private final List<AuthenticationProvider> providers;

    public AuthenticationManager(List<AuthenticationProvider> providers) {
        this.providers = providers;
    }

    public AuthenticationManager(AuthenticationProvider... providers) {
        this(List.of(providers));
    }

    public Authentication authenticate(Authentication authenticationRequest) {
        final AuthenticationProvider provider = providers.stream()
            .filter(it -> it.supports(authenticationRequest))
            .findFirst()
            .orElseThrow(() -> new AuthenticationException());

        return provider.authenticate(authenticationRequest);
    }

}
