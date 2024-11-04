package nextstep.security.configuration.authentication;

import lombok.RequiredArgsConstructor;
import nextstep.app.ui.AuthenticationException;
import nextstep.security.model.UserDetails;
import nextstep.security.service.UserDetailsService;

@RequiredArgsConstructor
public class DaoAuthenticationProvider implements AuthenticationProvider{

    private final UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetails userDetails =
                userDetailsService.loadUserByUsernameAndEmail(
                        authentication.getPrincipal().toString(),
                        authentication.getCredentials().toString())
                        .orElseThrow(AuthenticationException::new);

        return UsernamePasswordAuthenticationToken.authenticated(userDetails.getUserName(), userDetails.getPassword());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
