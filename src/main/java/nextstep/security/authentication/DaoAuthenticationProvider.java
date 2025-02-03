package nextstep.security.authentication;

import nextstep.security.UserDetails;
import nextstep.security.UserDetailsService;
import nextstep.security.exception.AuthenticationException;
import nextstep.security.util.PasswordEncoder;

public class DaoAuthenticationProvider implements AuthenticationProvider {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    public DaoAuthenticationProvider(final PasswordEncoder passwordEncoder, final UserDetailsService userDetailsService) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        UserDetails user = retrieveUser(authentication);

        return createSuccessAuthentication(user, authentication);
    }

    private UserDetails retrieveUser(final Authentication authentication) {
        return this.userDetailsService.loadUserByUsername(authentication.getPrincipal());
    }

    private Authentication createSuccessAuthentication(final UserDetails user, final Authentication authentication) {
        return UsernamePasswordAuthenticationToken.authenticated(user.getUsername(), authentication.getCredentials());
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
