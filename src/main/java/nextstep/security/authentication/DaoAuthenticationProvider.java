package nextstep.security.authentication;

import nextstep.security.Authentication;
import nextstep.security.AuthenticationProvider;
import nextstep.security.UserDetails;
import nextstep.security.UserDetailsService;
import nextstep.security.exception.BadCredentialsException;
import nextstep.security.exception.UsernameNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;

public class DaoAuthenticationProvider implements AuthenticationProvider {
    private static final Logger logger = LoggerFactory.getLogger(DaoAuthenticationProvider.class);
    private final UserDetailsService userDetailsService;

    public DaoAuthenticationProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        try {
            UserDetails user = tryRetrieveUser((String) authentication.getPrincipal());
            checkPassword(user, (String) authentication.getCredentials());

            return UsernamePasswordAuthenticationToken.authenticated(user);
        } catch (UsernameNotFoundException e) {
            logger.error("Fail to retrieve user", e);
            throw e;
        } catch (BadCredentialsException e) {
            logger.error("Bad credentials", e);
            throw e;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ClassUtils.isAssignable(UsernamePasswordAuthenticationToken.class, authentication);
    }

    private UserDetails tryRetrieveUser(String principal) {
        try {
            return userDetailsService.loadUserByUsername(principal);
        } catch (Exception e) {
            logger.error("Fail to get Authentication user", e);
            throw new UsernameNotFoundException("Fail to get Authentication user", e);
        }
    }

    private void checkPassword(UserDetails user, String credentials) {
        if (!user.getPassword().equals(credentials)) {
            throw new BadCredentialsException("Bad credentials");
        }
    }
}
