package nextstep.security.authentication.token.basic;

import nextstep.security.authentication.token.UsernamePasswordAuthenticationToken;
import nextstep.security.exception.AuthenticationTokenException;

import java.util.Base64;

public class BasicAuthenticationToken extends UsernamePasswordAuthenticationToken {
    public BasicAuthenticationToken(String username, String password) {
        super(username, password);
    }

    public static BasicAuthenticationToken of(String authorizationHeader) {
        try {
            final String[] usernameAndPassword = decode(
                    authorizationHeader.trim().split(" ")[1]
            ).split(":");
            return new BasicAuthenticationToken(usernameAndPassword[0], usernameAndPassword[1]);
        } catch (Exception e) {
            throw new AuthenticationTokenException();
        }
    }

    public static boolean supports(String authorizationHeader) {
        return authorizationHeader != null
                && authorizationHeader.trim().startsWith("Basic ");
    }

    private static String decode(String token) {
        return new String(Base64.getDecoder().decode(token));
    }
}
