package nextstep.security.authentication.token;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import nextstep.security.exception.AuthenticationTokenException;

import java.util.Base64;

public class BasicAuthenticationTokenConverter implements AuthenticationTokenConverter {
    private static final String AUTHORIZATION_HEADER = "Authorization";

    private BasicAuthenticationTokenConverter() {}

    public static AuthenticationTokenConverter getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public Authentication convert(HttpServletRequest request) {
        try {
            final byte[] decoded = Base64.getDecoder().decode(
                    request.getHeader(AUTHORIZATION_HEADER).trim().split(" ")[1]
            );
            final String[] usernameAndPassword = new String(decoded).split(":");
            return new UsernamePasswordAuthenticationToken(usernameAndPassword[0], usernameAndPassword[1]);
        } catch (Exception e) {
            throw new AuthenticationTokenException();
        }
    }

    @Override
    public boolean supports(HttpServletRequest request) {
        final String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        return authorizationHeader != null
                && authorizationHeader.trim().startsWith("Basic ");
    }

    private static final class SingletonHolder {
        private static final BasicAuthenticationTokenConverter INSTANCE = new BasicAuthenticationTokenConverter();
    }
}
