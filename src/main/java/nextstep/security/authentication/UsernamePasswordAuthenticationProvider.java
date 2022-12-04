package nextstep.security.authentication;

import nextstep.security.exception.AuthenticationException;
import nextstep.security.userdetils.UserDetails;
import nextstep.security.userdetils.UserDetailsService;

public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;

    public UsernamePasswordAuthenticationProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getPrincipal().toString());

        if (!userDetails.getPassword().equals(authentication.getCredentials())) {
            throw new AuthenticationException();
        }

        return UsernamePasswordAuthenticationToken.authenticated(
            userDetails.getUsername(),
            userDetails.getPassword()
        );
    }
}
