package nextstep.security.config;

import nextstep.security.ProviderNotFoundException;

import java.util.Collections;
import java.util.List;

public class ProviderManager implements AuthenticationManager {

    private List<AuthenticationProvider> providers = Collections.emptyList();

    public ProviderManager(List<AuthenticationProvider> providers) {
        this.providers = providers;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        Class<? extends Authentication> authenticationClassToTest = authentication.getClass();
        Authentication result = null;
        for (AuthenticationProvider provider : this.providers) {
            if (!provider.supports(authenticationClassToTest)) {
                continue;
            }
            result = provider.authenticate(authentication);
            if (result != null) { // 인증된 객제가 있다면
                copyDetails(authentication, result);
                break;
            }
        }
        if (result == null) {
            throw new ProviderNotFoundException("지원하는 provider가 없습니다!");
        }
        return result;
    }

    // 인증된 토큰에 detail 값이 없다면 detail 넣어주기
    private void copyDetails(Authentication source, Authentication dest) {
        if ((dest instanceof AbstractAuthenticationToken token) && (dest.getDetails() == null)) {
            token.setDetails(source.getDetails());
        }
    }

}

