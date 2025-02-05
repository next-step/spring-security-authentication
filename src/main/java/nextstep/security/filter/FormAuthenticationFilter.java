package nextstep.security.filter;

import nextstep.security.authentication.token.form.FormAuthenticationTokenConverter;
import nextstep.security.user.UserDetailsService;

public class FormAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    public FormAuthenticationFilter(UserDetailsService userDetailsService, String uri) {
        super(userDetailsService, FormAuthenticationTokenConverter.getInstance(), uri);
    }
}
