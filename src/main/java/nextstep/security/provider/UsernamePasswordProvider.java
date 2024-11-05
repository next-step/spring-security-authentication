package nextstep.security.provider;

import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.UsernamePasswordAuthenticationToken;
import nextstep.security.service.UserDetailsService;
import nextstep.security.userdetails.UserDetails;

public class UsernamePasswordProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;

    public UsernamePasswordProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) authentication;

        UserDetails userDetails = userDetailsService.loadUserByEmailAndPassword((String) usernamePasswordAuthenticationToken.getPrincipal(), (String) usernamePasswordAuthenticationToken.getCredentials());
        return new UsernamePasswordAuthenticationToken(userDetails, null);
    }
}
