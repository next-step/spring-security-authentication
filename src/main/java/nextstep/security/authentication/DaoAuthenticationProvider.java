package nextstep.security.authentication;

import nextstep.security.exception.BadCredentialsException;
import nextstep.security.exception.UsernameNotFoundException;
import nextstep.security.userdetails.UserDetails;
import nextstep.security.userdetails.UserDetailsService;
import org.springframework.util.ClassUtils;

public class DaoAuthenticationProvider implements AuthenticationProvider {
    private final UserDetailsService userDetailsService;

    public DaoAuthenticationProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        UserDetails user = tryRetrieveUser((String) authentication.getPrincipal());
        checkPassword(user, (String) authentication.getCredentials());

        return UsernamePasswordAuthenticationToken.authenticated(user);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ClassUtils.isAssignable(UsernamePasswordAuthenticationToken.class, authentication);
    }

    private UserDetails tryRetrieveUser(String principal) {
        try {
            return userDetailsService.loadUserByUsername(principal);
        } catch (Exception e) {
            throw new UsernameNotFoundException("Fail to get Authentication user", e);
        }
    }

    private void checkPassword(UserDetails user, String credentials) {
        if (!user.getPassword().equals(credentials)) {
            throw new BadCredentialsException("Bad credentials");
        }
    }
}
