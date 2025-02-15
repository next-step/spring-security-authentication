package nextstep.authentication;

import nextstep.authentication.exception.AuthenticationException;
import nextstep.authentication.password.PasswordEncoder;

public class DaoAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public DaoAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = determineUsername(authentication);
        UserDetails user = retrieveUser(username);

        additionalAuthenticationChecks(user, (UsernamePasswordAuthenticationToken) authentication);

        return createSuccessAuthentication(user.getUsername(), authentication, user);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private String determineUsername(Authentication authentication) {
        return authentication.getPrincipal() == null ? "NONE_PROVIDED" : authentication.getPrincipal();
    }

    private UserDetails retrieveUser(String username) throws AuthenticationException {

        try {
            UserDetails loadedUser = userDetailsService.loadUserByUsername(username);
            if (loadedUser == null) {
                throw new AuthenticationException("UserDetailsService returned null, which is an interface contract violation");
            } else {
                return loadedUser;
            }
        } catch (Exception ex) {
            throw new AuthenticationException(ex.getMessage(), ex);
        }
    }

    private void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            throw new AuthenticationException("Bad credentials Exception");
        } else {
            String presentedPassword = authentication.getCredentials();
            if (!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
                throw new AuthenticationException("비밀번호가 일치하지 않습니다.");
            }
        }
    }

    private Authentication createSuccessAuthentication(String principal, Authentication authentication, UserDetails user) {
        return UsernamePasswordAuthenticationToken.authenticated(principal, authentication.getCredentials());
    }
}
