package nextstep.security.authentication;

import nextstep.app.ui.AuthenticationException;
import nextstep.security.UserDetails;
import nextstep.security.UserDetailsService;

import java.util.Objects;

public class DaoAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;

    public DaoAuthenticationProvider(final UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        final UserDetails userDetails = userDetailsService.loadUserByUsername((String) authentication.getPrincipal());

        if (!Objects.equals(userDetails.getPassword(), authentication.getCredentials())) {
            throw new AuthenticationException();
        }

        return new UsernamePasswordAuthenticationToken(userDetails.getPassword(), userDetails.getUsername(), true);
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
