package nextstep.security.authentication;

import nextstep.security.UserDetails;
import nextstep.security.UserDetailsService;

import java.util.Objects;

public class DaoAuthenticationProvider implements AuthenticationProvider {
    private final UserDetailsService userDetailsService;

    public DaoAuthenticationProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        check(authentication);

        final String username = authentication.getPrincipal().toString();
        final String password = authentication.getCredentials().toString();
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (Objects.equals(password, userDetails.getPassword())) {
            return UsernamePasswordAuthenticationToken.ofAuthenticated(username, password);
        }

        throw new AuthenticationException();
    }

    private static void check(Authentication authentication) {
        if (authentication == null) {
            throw new AuthenticationException();
        }
        if (authentication.getPrincipal() == null) {
            throw new AuthenticationException();
        }
        if (authentication.getCredentials() == null) {
            throw new AuthenticationException();
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication == UsernamePasswordAuthenticationToken.class;
    }
}
