package nextstep.security.authentication;

import nextstep.security.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class DaoAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;

    public DaoAuthenticationProvider(final UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(final Authentication authentication) {
        var userDetails = userDetailsService.findUserDetailsByPrincipal(authentication.getPrincipal());
        if (!userDetails.match(authentication.getCredentials())) {
            return UsernamePasswordAuthenticationToken.unauthenticated(authentication.getPrincipal(), authentication.getCredentials());
        }
        return UsernamePasswordAuthenticationToken.authenticated(authentication.getPrincipal(), authentication.getCredentials());
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return authentication == UsernamePasswordAuthenticationToken.class;
    }
}
