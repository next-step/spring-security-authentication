package nextstep.security.service.authentication;

import java.util.List;

import lombok.RequiredArgsConstructor;
import nextstep.security.model.authentication.Authentication;

@RequiredArgsConstructor
public class ProviderManager implements AuthenticationManager{

    private final List<AuthenticationProvider> authenticationProviders;

    @Override
    public Authentication authenticate(Authentication authentication) {
        for (AuthenticationProvider provider : authenticationProviders) {
            if (provider.supports(authentication.getClass())) {
                return provider.authenticate(authentication);
            }
        }
        return null;
    }
}
