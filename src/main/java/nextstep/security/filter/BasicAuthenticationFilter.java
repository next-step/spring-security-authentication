package nextstep.security.filter;

import nextstep.security.authentication.token.basic.BasicAuthenticationTokenConverter;
import nextstep.security.user.UserDetailsService;

public class BasicAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    public BasicAuthenticationFilter(UserDetailsService userDetailsService) {
        super(userDetailsService, BasicAuthenticationTokenConverter.getInstance());
    }
}
