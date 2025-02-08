package nextstep.security.config;

import nextstep.security.AuthenticationException;
import nextstep.security.UserDetailService;
import nextstep.security.UserDetails;

public class DaoAuthenticationProvider implements AuthenticationProvider{

    private final UserDetailService userDetailService;
    private final PasswordEncoder passwordEncoder;

    public DaoAuthenticationProvider(UserDetailService userDetailService, PasswordEncoder passwordEncoder) {
        this.userDetailService = userDetailService;
        this.passwordEncoder = passwordEncoder;
    }

    public DaoAuthenticationProvider(UserDetailService userDetailService) {
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

        UserDetails user = userDetailService.getUserByUsername(username);
        if (!password.equals(user.getPassword())) {
            throw new AuthenticationException("Passwords do not match");
        }
        return new UsernamePasswordAuthenticationToken(username, password, user);
    }

    @Override
    public boolean supports(Class<?> authentication) { // todo 왜 instanceOf가 아닌 이렇게 클래스로 비교를 하는걸까?
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
