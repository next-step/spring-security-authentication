package nextstep.app;

import nextstep.security.*;
import nextstep.security.exception.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class DaoAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;

    public DaoAuthenticationProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public UsernamePasswordAuthenticationToken authenticate(Authentication authentication) throws AuthenticationException {
        UserDetails userDetails = userDetailsService.loadUserDetailsByUserName(authentication.getPrincipal().toString());
        if (!Objects.equals(userDetails.password(), authentication.getCredentials())) {
            throw new AuthenticationException();
        }
        return UsernamePasswordAuthenticationToken.authorizedToken(userDetails.username(), userDetails.password());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
