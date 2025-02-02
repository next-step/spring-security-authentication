package nextstep.security.authentication.converter.form;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.converter.AuthenticationConverter;

public class FormAuthenticationConverter implements AuthenticationConverter {
    @Override
    public Authentication convert(HttpServletRequest request) {
        return FormAuthentication.of(request.getParameterMap());
    }

    @Override
    public boolean supports(HttpServletRequest request) {
        return FormAuthentication.supports(request.getParameterMap());
    }
}
