package nextstep.security.authentication;

import java.util.List;

public class ProviderManager implements AuthenticationManager{

    private final List<AuthenticationProvider> providers;

    public ProviderManager(List<AuthenticationProvider> providers) {
        System.out.println("ProviderManager.ProviderManager");
        this.providers = providers;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        System.out.println("ProviderManager.authenticate");
        for (AuthenticationProvider provider : providers) {
            System.out.println("provider = " + provider);
            if(provider.supports(authentication.getClass())) {
                System.out.println("ProviderManager.authenticate");
                return provider.authenticate(authentication);
            }
        }
        return null;
    }
}
