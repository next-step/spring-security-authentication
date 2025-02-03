package nextstep.security.authentication.token.basic;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.token.AuthenticationTokenConverter;

public class BasicAuthenticationTokenConverter implements AuthenticationTokenConverter {
    private static final String AUTHORIZATION_HEADER = "Authorization";

    private BasicAuthenticationTokenConverter() {}

    public static AuthenticationTokenConverter getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public Authentication convert(HttpServletRequest request) {
        return BasicAuthenticationToken.of(request.getHeader(AUTHORIZATION_HEADER));
    }

    @Override
    public boolean supports(HttpServletRequest request) {
        return BasicAuthenticationToken.supports(
                request.getHeader(AUTHORIZATION_HEADER)
        );
    }

    private static final class SingletonHolder {
        private static final BasicAuthenticationTokenConverter INSTANCE = new BasicAuthenticationTokenConverter();
    }
}
