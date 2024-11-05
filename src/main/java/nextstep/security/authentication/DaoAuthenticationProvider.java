package nextstep.security.authentication;

import nextstep.security.exception.AuthenticationException;
import nextstep.security.userdetail.UserDetail;
import nextstep.security.userdetail.UserDetailService;

public class DaoAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailService userDetailService;

    public DaoAuthenticationProvider(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        UserDetail userDetail = userDetailService.getUserDetail(
                authentication.getPrincipal().toString());
        if (userDetail.verifyPassword(authentication.getCredentials().toString())) {
            throw new AuthenticationException();
        }
        return UsernamePasswordAuthenticationToken.authenticated(userDetail.getUsername(),
                userDetail.getPassword());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
