package nextstep.security.authentication.provider;

import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.token.UsernamePasswordAuthenticationToken;
import nextstep.security.exception.AuthenticationException;
import nextstep.security.user.UserDetails;
import nextstep.security.user.UserDetailsService;

public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {
    private final UserDetailsService userDetailsService;

    public UsernamePasswordAuthenticationProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authenticationToken) throws AuthenticationException {
        final UserDetails userDetails = userDetailsService.loadUserByUsername(
                authenticationToken.getPrincipal().toString()
        );
        if (!userDetails.getPassword().equals(
                authenticationToken.getCredentials().toString()
        )) {
            throw AuthenticationException.invalidCredentials();
        }
        return new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(), userDetails.getPassword()
        );
    }

    @Override
    public boolean supports(Class<?> authenticationToken) {
        return UsernamePasswordAuthenticationToken.class
                .isAssignableFrom(authenticationToken);
    }
}
