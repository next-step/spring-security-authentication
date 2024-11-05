package nextstep.security.provider;

import lombok.RequiredArgsConstructor;
import nextstep.app.ui.AuthenticationException;
import nextstep.security.credential.UsernamePasswordAuthenticationToken;
import nextstep.security.model.SecurityAuthentication;
import nextstep.security.model.UserDetails;
import nextstep.security.service.UserDetailService;

import java.util.Objects;

@RequiredArgsConstructor
public class UsernameProvider implements AuthenticationProvider {
    private final UserDetailService userDetailService;

    @Override
    public SecurityAuthentication authenticate(SecurityAuthentication authentication) {
        UserDetails userDetails = userDetailService.loadUserByUsername(authentication.getPrincipal().toString());
        if (!Objects.equals(userDetails.getPassword(), authentication.getCredentials())) {
            throw new AuthenticationException();
        }
        return UsernamePasswordAuthenticationToken.authenticated(userDetails.getUsername(), userDetails.getPassword());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
