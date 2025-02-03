package nextstep.security.basic;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.Authentication;
import nextstep.security.AuthenticationConverter;
import nextstep.security.authentication.UsernamePasswordAuthenticationToken;
import nextstep.security.exception.BadCredentialsException;
import nextstep.security.util.Base64Convertor;

public class BasicAuthenticationConverter implements AuthenticationConverter {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BASIC_PREFIX = "Basic ";

    @Override
    public Authentication convert(HttpServletRequest request) {
        String authorization = request.getHeader(AUTHORIZATION_HEADER);
        if (authorization == null || !authorization.startsWith(BASIC_PREFIX)) {
            return null;
        }

        String credentials = authorization.substring(BASIC_PREFIX.length());
        String decodedToken = Base64Convertor.decode(credentials);

        int delim = decodedToken.indexOf(":");
        if (delim == -1) {
            throw new BadCredentialsException("Invalid basic authentication token");
        }

        String username = decodedToken.substring(0, delim);
        String password = decodedToken.substring(delim + 1);

        return new UsernamePasswordAuthenticationToken(username, password);
    }
}
