package nextstep.security.authentication;

import nextstep.security.userdetails.UserDetails;
import nextstep.security.userdetails.UserDetailsService;

import java.util.Objects;

public class DaoAuthenticationProvider implements AuthenticationProvider{

    private final UserDetailsService userDetailsService;

    public DaoAuthenticationProvider(UserDetailsService userDetailsService) {
        System.out.println("DaoAuthenticationProvider.DaoAuthenticationProvider");
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        System.out.println("DaoAuthenticationProvider.authenticate");
        UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getPrincipal().toString());
        if(!Objects.equals(userDetails.getPassword(), authentication.getCredentials())) {
            System.out.println("userDetails null");
            throw new AuthenticationException();
        }
        return UsernamePasswordAuthenticationToken.authenticated(userDetails.getUsername(), userDetails.getPassword());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        System.out.println("DaoAuthenticationProvider.supports");
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
