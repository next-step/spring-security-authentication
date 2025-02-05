package nextstep.security.authentication;

import nextstep.app.util.Base64Convertor;
import nextstep.security.authentication.exception.AuthenticationException;
import nextstep.security.user.UsernamePasswordAuthenticationToken;
import org.springframework.util.StringUtils;

public class AuthenticationConverter {


    public Authentication convert(String headerValue) {
        if (!StringUtils.hasText(headerValue)) {
            throw new AuthenticationException();
        }

        String credentials = headerValue.split(" ")[1];
        String decodedString = Base64Convertor.decode(credentials);
        String[] usernameAndPassword = decodedString.split(":");
        if (usernameAndPassword.length != 2) {
            throw new AuthenticationException();
        }

        return UsernamePasswordAuthenticationToken.unAuthorizedToken(usernameAndPassword[0], usernameAndPassword[1]);
    }
}
