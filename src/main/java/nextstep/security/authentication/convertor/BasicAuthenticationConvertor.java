package nextstep.security.authentication.convertor;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.exceptions.AuthenticationException;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.UsernamePasswordAuthenticationToken;
import nextstep.security.util.Base64Convertor;
import nextstep.security.util.string.ColonSeparatedParser;
import org.springframework.http.HttpHeaders;

public class BasicAuthenticationConvertor implements AuthenticationConvertor {
    @Override
    public Authentication convert(final HttpServletRequest request) {
        var authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null) {
            return null;
        }
        var credentials = authorization.split(" ")[1];
        var decodedString = Base64Convertor.decode(credentials);
        var usernameAndPasswordPair = ColonSeparatedParser.parse(decodedString);
        if (usernameAndPasswordPair == null) {
            throw new AuthenticationException();
        }
        return UsernamePasswordAuthenticationToken.unauthenticated(
                usernameAndPasswordPair.getFirst(), usernameAndPasswordPair.getSecond());
    }
}
