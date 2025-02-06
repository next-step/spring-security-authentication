package nextstep.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.UsernamePasswordAuthenticationToken;
import nextstep.security.core.context.SecurityContext;
import nextstep.security.core.context.SecurityContextHolder;
import nextstep.security.exception.AuthenticationException;
import nextstep.security.util.Base64Convertor;

import java.io.IOException;

public class BasicAuthFilter extends AbstractAuthProcessingFilter {
    public static final String BASIC_HEADER_COLON = ":";
    public static final String BASIC_HEADER_COMMA = " ";
    public static final String BASIC_HEADER_PREFIX = "Basic ";
    public static final String AUTHORIZATION = "Authorization";

    public BasicAuthFilter(final AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    boolean match(final HttpServletRequest request) {
        return existAuthorizationHeader(request);
    }

    private boolean existAuthorizationHeader(final HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION) != null;
    }

    @Override
    public Authentication makeAuthentication(final HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if (isValidBasicAuthHeader(authorizationHeader)) {
            throw new AuthenticationException();
        }

        String credentials = extractCredentials(authorizationHeader);
        String[] usernameAndPassword = parseCredentials(credentials);

        return UsernamePasswordAuthenticationToken.unauthenticated(usernameAndPassword[0], usernameAndPassword[1]);
    }

    private boolean isValidBasicAuthHeader(final String authorizationHeader) {
        return authorizationHeader == null || !authorizationHeader.startsWith(BASIC_HEADER_PREFIX);
    }

    private String extractCredentials(String authorizationHeader) {
        String[] parts = authorizationHeader.split(BASIC_HEADER_COMMA);
        if (parts.length != 2) {
            throw new AuthenticationException();
        }

        return Base64Convertor.decode(parts[1]);
    }

    private String[] parseCredentials(String decodedString) {
        String[] usernameAndPassword = decodedString.split(BASIC_HEADER_COLON);
        if (usernameAndPassword.length != 2) {
            throw new AuthenticationException();
        }

        return usernameAndPassword;
    }

    @Override
    protected void successAuthentication(final HttpServletRequest request, final HttpServletResponse response,
                                         final FilterChain filterChain) throws ServletException, IOException {
        final SecurityContext context = SecurityContextHolder.getContext();
        final Authentication authentication = context.getAuthentication();
        authentication.getAuthorities().clear();
    }
}
