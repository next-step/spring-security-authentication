package nextstep.security.authentication.provider;

import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.UsernamePasswordAuthenticationToken;
import nextstep.security.exception.AuthenticationException;
import nextstep.security.user.UserDetails;
import nextstep.security.user.UserDetailsService;
import nextstep.security.util.PasswordsEncoder;

public class DaoAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordsEncoder passwordsEncoder;

    public DaoAuthenticationProvider(UserDetailsService userDetailsService, PasswordsEncoder passwordsEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordsEncoder = passwordsEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        UserDetails userDetails = retrieveUser(
                authentication.getPrincipal().toString(),
                authentication.getCredentials().toString()
        );
        return UsernamePasswordAuthenticationToken.authenticated(
                userDetails.getUsername(),
                userDetails.getPassword());
    }

    private UserDetails retrieveUser(String username, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (!userDetails.getPassword().equals(password)) {
            throw new AuthenticationException("비밀번호가 일치하지 않습니다.");
        }
        return userDetails;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
