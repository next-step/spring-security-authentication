package nextstep.security.authentication;

import nextstep.security.core.uesrdetails.UserDetails;
import nextstep.security.core.uesrdetails.UserDetailsService;
import nextstep.security.exception.AuthenticationException;
import nextstep.security.util.PasswordMatcher;

public class DaoAuthenticationProvider implements AuthenticationProvider {
    private final PasswordMatcher passwordMatcher;
    private final UserDetailsService userDetailsService;

    public DaoAuthenticationProvider(final PasswordMatcher passwordMatcher, final UserDetailsService userDetailsService) {
        this.passwordMatcher = passwordMatcher;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        UserDetails user = retrieveUser(authentication);

        validatePassword(authentication, user);

        return createSuccessAuthentication(user, authentication);
    }

    private void validatePassword(final Authentication authentication, final UserDetails user) {
        if (!passwordMatcher.matches(authentication.getCredentials(), user.getPassword())) {
            throw new AuthenticationException();
        }
    }

    private UserDetails retrieveUser(final Authentication authentication) {
        return this.userDetailsService.loadUserByUsername(authentication.getPrincipal());
    }

    private Authentication createSuccessAuthentication(final UserDetails user, final Authentication authentication) {
        return UsernamePasswordAuthenticationToken.authenticated(user.getUsername(), authentication.getCredentials(), user.getAuthorities());
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
