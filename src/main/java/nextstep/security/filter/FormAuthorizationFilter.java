package nextstep.security.filter;

import nextstep.security.authentication.converter.form.FormAuthenticationConverter;
import nextstep.security.user.UserDetailsService;

public class FormAuthorizationFilter extends UsernamePasswordAuthorizationFilter {
    public FormAuthorizationFilter(UserDetailsService userDetailsService, String uri) {
        super(userDetailsService, FormAuthenticationConverter.getInstance(), uri);
    }
}
