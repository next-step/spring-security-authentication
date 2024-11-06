package nextstep.security.provider;

import lombok.RequiredArgsConstructor;
import nextstep.security.model.SecurityAuthentication;

import java.util.List;

@RequiredArgsConstructor
public class ProviderManager implements AuthenticationManager {
    private final List<AuthenticationProvider> providers;

    @Override
    public SecurityAuthentication authenticate(SecurityAuthentication authentication) {
        for (AuthenticationProvider provider : providers) {
            if (provider.supports(authentication.getClass())) {
                return provider.authenticate(authentication);
            }
        }
        return null;
    }
}
