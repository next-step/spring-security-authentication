package nextstep.security.filter;

import nextstep.security.authentication.converter.basic.BasicAuthenticationConverter;
import nextstep.security.user.UserDetailsService;

public class BasicAuthorizationFilter extends UsernamePasswordAuthorizationFilter {
    public BasicAuthorizationFilter(UserDetailsService userDetailsService) {
        super(userDetailsService, BasicAuthenticationConverter.getInstance());
    }
}
