package nextstep.security.authentication.converter.basic;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.converter.AuthenticationConverter;

public class BasicAuthenticationConverter implements AuthenticationConverter {
    private static final String AUTHORIZATION_HEADER = "Authorization";

    private BasicAuthenticationConverter() {}
    
    public static AuthenticationConverter getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public Authentication convert(HttpServletRequest request) {
        return BasicAuthentication.of(request.getHeader(AUTHORIZATION_HEADER));
    }

    @Override
    public boolean supports(HttpServletRequest request) {
        final String header = request.getHeader(AUTHORIZATION_HEADER);
        return header != null && header.trim().startsWith("Basic ");
    }

    private static class SingletonHolder {
        private static final BasicAuthenticationConverter INSTANCE = new BasicAuthenticationConverter();
    }
}
