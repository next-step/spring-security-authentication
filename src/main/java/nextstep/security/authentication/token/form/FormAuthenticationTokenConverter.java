package nextstep.security.authentication.token.form;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.token.AuthenticationTokenConverter;

public class FormAuthenticationTokenConverter implements AuthenticationTokenConverter {
    private FormAuthenticationTokenConverter() {}

    public static AuthenticationTokenConverter getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public Authentication convert(HttpServletRequest request) {
        return FormAuthenticationToken.of(request.getParameterMap());
    }

    @Override
    public boolean supports(HttpServletRequest request) {
        return FormAuthenticationToken.supports(request.getParameterMap());
    }

    private static final class SingletonHolder {
        private static final FormAuthenticationTokenConverter INSTANCE = new FormAuthenticationTokenConverter();
    }
}
