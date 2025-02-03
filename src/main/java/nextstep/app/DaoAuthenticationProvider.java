package nextstep.app;

import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationProvider;
import nextstep.security.authentication.exception.AuthenticationException;
import nextstep.security.user.UserDetails;
import nextstep.security.user.UserDetailsService;
import nextstep.security.user.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class DaoAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;

    public DaoAuthenticationProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = Objects.requireNonNull(userDetailsService);
    }

    @Override
    public UsernamePasswordAuthenticationToken authenticate(Authentication authentication) throws AuthenticationException {
        UserDetails userDetails = userDetailsService.loadUserDetailsByUserName(authentication.getPrincipal().toString());
        if (!Objects.equals(userDetails.password(), authentication.getCredentials())) {
            throw new AuthenticationException();
        }

        return UsernamePasswordAuthenticationToken.authorizedToken(userDetails.username(), userDetails.password(), userDetails.authorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
