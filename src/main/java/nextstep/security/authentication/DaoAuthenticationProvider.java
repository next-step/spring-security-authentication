package nextstep.security.authentication;

import nextstep.security.exception.AuthenticationException;
import nextstep.security.user.UserDetails;
import nextstep.security.user.UserDetailsService;

public class DaoAuthenticationProvider implements AuthenticationProvider {
    private final UserDetailsService userDetailsService;

    public DaoAuthenticationProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getPrincipal());
        if (!userDetails.getPassword().equals(authentication.getCredentials())) {
            throw new AuthenticationException();
        }
        authentication.setAuthenticated(true);
        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
