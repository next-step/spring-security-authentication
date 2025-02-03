package nextstep.security.authentication;

import nextstep.security.exception.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

public class DaoAuthenticationProvider implements AuthenticationProvider {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    public DaoAuthenticationProvider(final PasswordEncoder passwordEncoder, final UserDetailsService userDetailsService) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }


    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        // userDetailsService 호출 후 UserDetails 리턴 || UserDetails user = retrieveUser();

        // token 새로 만들어서 리턴 || createSuccessAuthentication()
        return null;
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
