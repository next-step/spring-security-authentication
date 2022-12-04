package nextstep.security.support;

import nextstep.security.authentication.BasicAuthenticationToken;
import nextstep.security.exception.AuthenticationException;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import javax.servlet.http.HttpServletRequest;

@Component
public class BasicAuthenticationDecoder {

    public static final String AUTHORIZATION = "Basic";
    public static final String SPLIT_CHAR = ":";

    public BasicAuthenticationToken decode(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(!authorization.startsWith(AUTHORIZATION)) {
            throw new AuthenticationException();
        }
        String decodeTarget = authorization.substring(AUTHORIZATION.length()).trim();
        String decodeValue = new String(Base64Utils.decode(decodeTarget.getBytes()));

        String[] emailAndPassword = decodeValue.split(SPLIT_CHAR);
        String email = emailAndPassword[0];
        String password = emailAndPassword[1];

        return new BasicAuthenticationToken(email, password);
    }
}
