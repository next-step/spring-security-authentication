package nextstep.security.config;

import nextstep.security.AuthenticationException;
import nextstep.security.UserDetailService;
import nextstep.security.UserDetails;

public class BasicAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailService userDetailService;
    private final PasswordEncoder passwordEncoder;

    public BasicAuthenticationProvider(UserDetailService userDetailService, PasswordEncoder passwordEncoder) {
        this.userDetailService = userDetailService;
        this.passwordEncoder = passwordEncoder;
    }

    public BasicAuthenticationProvider(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
        this.passwordEncoder = null;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!supports(authentication.getClass())) {
            return null;
        }
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        UserDetails userDetail = userDetailService.getUserByUsername(username);
        if (!userDetail.getPassword().equals(password)) {
            throw new AuthenticationException();
        }
        return new BasicAuthenticationToken(username, password);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return BasicAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
