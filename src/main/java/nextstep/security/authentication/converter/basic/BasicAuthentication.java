package nextstep.security.authentication.converter.basic;

import nextstep.security.authentication.token.UsernamePasswordAuthenticationToken;
import nextstep.security.exception.AuthenticationException;

import java.util.Base64;

public class BasicAuthentication extends UsernamePasswordAuthenticationToken {
    public BasicAuthentication(String username, String password) {
        super(username, password);
    }

    public static BasicAuthentication of(String authorizationHeader) {
        try {
            final String[] usernameAndPassword = decode(
                    authorizationHeader.trim().split(" ")[1]
            ).split(":");
            return new BasicAuthentication(usernameAndPassword[0], usernameAndPassword[1]);
        } catch (Exception e) {
            throw AuthenticationException.badToken();
        }
    }

    private static String decode(String token) {
        return new String(Base64.getDecoder().decode(token));
    }
}
