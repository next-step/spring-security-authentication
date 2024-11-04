package nextstep.security.core.authentication.provider;

import lombok.RequiredArgsConstructor;
import nextstep.security.core.SecurityPrincipal;
import nextstep.security.core.authentication.Authentication;
import nextstep.security.core.authentication.AuthenticationProvider;
import nextstep.security.core.userdetails.UserDetail;
import nextstep.security.core.userdetails.UserDetailService;
import nextstep.security.exception.AuthenticationException;

import java.util.List;

@RequiredArgsConstructor
public class AbstractUserDetailsAuthenticationProvider implements AuthenticationProvider {

    static private final List<SecurityPrincipal> supports = List.of(SecurityPrincipal.BASIC_TOKEN_AUTHENTICATION, SecurityPrincipal.USERNAME_PASSWORD_AUTHENTICATION);

    private final UserDetailService userDetailService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication.getCredential();
            UserDetail user = userDetailService.findUserByUsername(token.getUsername());
            authentication.setAuthenticated(false);
            if (user != null && user.getPassword().equals(token.getPassword())) {
                authentication.setAuthenticated(true);
            }
        }
        catch (Exception e){
            authentication.setAuthenticated(false);
        }
        finally {
            return authentication;
        }
    }

    @Override
    public boolean supports(SecurityPrincipal securityPrincipal) {
        return supports.contains(securityPrincipal);
    }
}
