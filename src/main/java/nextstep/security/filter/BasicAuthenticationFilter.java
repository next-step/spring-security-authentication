package nextstep.security.filter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.provider.AuthenticationManager;
import nextstep.security.authentication.UsernamePasswordAuthenticationToken;
import nextstep.security.exception.AuthenticationException;
import nextstep.security.util.Base64Convertor;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;
import java.util.regex.Pattern;

public class BasicAuthenticationFilter extends AbstractAuthenticationFilter {

    private static final String AUTHENTICATION_SCHEME_BASIC = "Basic";
    private static final Pattern BASIC_AUTH_PATTERN = Pattern.compile("^Basic\\s+(.+)$");

    public BasicAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(!support(request)) {
            return null;
        }
        String[] token = extractBasicAuthCredentials(request);

        return this.authenticationManager.authentication(
                new UsernamePasswordAuthenticationToken(token[0], token[1]));
    }

    public static String[] extractBasicAuthCredentials(HttpServletRequest request) throws AuthenticationException {
        return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(header -> BASIC_AUTH_PATTERN.matcher(header).matches())
                .map(header -> header.replaceFirst("Basic\\s+", ""))
                .map(Base64Convertor::decode)
                .map(token -> token.split(":", 2))
                .filter(parts -> parts.length == 2)
                .orElseThrow(() -> new AuthenticationException("Invalid basic authentication token"));
    }

    @Override
    public boolean support(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null) {
            return false;
        }
        return StringUtils.startsWithIgnoreCase(header, AUTHENTICATION_SCHEME_BASIC);
    }

    private byte[] decode(byte[] base64Token) {
        try {
            return Base64.getDecoder().decode(base64Token);
        } catch (IllegalArgumentException ex) {
            throw new AuthenticationException("Failed to decode basic authentication token");
        }
    }
}

