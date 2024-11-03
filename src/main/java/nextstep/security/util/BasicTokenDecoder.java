package nextstep.security.util;

import static nextstep.security.util.SecurityConstants.BASIC_TOKEN_PREFIX;

import java.nio.charset.StandardCharsets;
import nextstep.security.exception.AuthenticationException;
import nextstep.security.userdetail.UserDetail;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

@Component
public class BasicTokenDecoder implements TokenDecoder {

    @Override
    public UserDetail decodeToken(String token) {
        String base64Token = token.substring(BASIC_TOKEN_PREFIX.length());
        String decodedToken = new String(Base64Utils.decodeFromString(base64Token),
                StandardCharsets.UTF_8);

        validateBasicToken(token);

        String[] parts = decodedToken.split(":");
        if (parts.length != 2) {
            throw new AuthenticationException();
        }
        return new UserDetail(parts[0], parts[1]);
    }

    private void validateBasicToken(String authorization) {
        if (authorization == null) {
            throw new AuthenticationException();
        }

        if (!authorization.startsWith(BASIC_TOKEN_PREFIX)) {
            throw new AuthenticationException();
        }
    }
}
