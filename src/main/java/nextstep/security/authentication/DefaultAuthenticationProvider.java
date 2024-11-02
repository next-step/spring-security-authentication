package nextstep.security.authentication;

import nextstep.app.service.UserDetail;
import nextstep.app.service.UserDetailService;

public class DefaultAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailService userDetailService;

    public DefaultAuthenticationProvider(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        UserDetail userDetail = userDetailService.findUserDetail(
                authentication.getPrincipal().toString());

        if (userDetail == null) {
            return null;
        }

        if (!userDetail.getPassword().equals(authentication.getCredentials().toString())) {
            return null;
        }

        return DefaultAuthentication.authenticated(
                userDetail.getEmail(), userDetail.getPassword());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return DefaultAuthentication.class.isAssignableFrom(authentication);
    }
}
