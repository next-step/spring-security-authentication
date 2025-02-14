package nextstep.authentication;

import nextstep.authentication.exception.AuthenticationException;
import nextstep.authentication.exception.ProviderNotFoundException;

import java.util.List;

public class ProviderManager implements AuthenticationManager {

    private final List<AuthenticationProvider> providers;

    public ProviderManager(List<AuthenticationProvider> providers) {
        this.providers = providers;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        Class<? extends Authentication> target = authentication.getClass();
        Authentication result = null;
        AuthenticationException lastException = null;

        for (AuthenticationProvider provider : this.providers) {
            if (provider.supports(target)) {

                try {
                    result = provider.authenticate(authentication);
                    if (result != null) {
                        break;
                    }
                } catch (AuthenticationException ex) {
                    lastException = ex;
                }
            }
        }

        if (result != null) {
            return result;
        }
        if (lastException == null) {
            lastException = new ProviderNotFoundException(String.format("No AuthenticationProvider found for %s", target.getName()));
        }

        throw lastException;
    }
}
