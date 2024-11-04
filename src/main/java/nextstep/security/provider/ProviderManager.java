package nextstep.security.provider;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class ProviderManager implements AuthenticationManager {
    private final Map<String, AuthenticationProvider> providers;

    @Override
    public AuthenticationProvider getProvider(String principal) {
        return providers.get(principal);
    }
}
