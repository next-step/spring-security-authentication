package nextstep.security.core.authentication;

import lombok.RequiredArgsConstructor;
import nextstep.security.core.SecurityPrincipal;
import nextstep.security.exception.AuthErrorCodes;
import nextstep.security.exception.AuthenticationException;

import java.util.List;

@RequiredArgsConstructor
public class AuthenticationManager {
    private final List<AuthenticationProvider> authenticationProviders;

    public AuthenticationProvider getAuthenticationProvider(SecurityPrincipal principal){
        return authenticationProviders.stream().filter(provider -> provider.supports(principal)).findAny().orElseThrow(
                ()-> new AuthenticationException(AuthErrorCodes.UNAUTHORIZED_LOGIN_REQUEST)
        );
    }
}
