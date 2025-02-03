package nextstep.security.filter;

import nextstep.security.authentication.token.basic.BasicAuthenticationTokenConverter;
import nextstep.security.user.UserDetailsService;

public class BasicAuthorizationFilter extends UsernamePasswordAuthorizationFilter {
    public BasicAuthorizationFilter(UserDetailsService userDetailsService) {
        super(userDetailsService, BasicAuthenticationTokenConverter.getInstance());
    }
}
