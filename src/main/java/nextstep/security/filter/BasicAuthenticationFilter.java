package nextstep.security.filter;

import nextstep.security.authentication.*;
import nextstep.security.domain.UserDetailsService;
import nextstep.security.exception.AuthenticationException;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Base64Utils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class BasicAuthenticationFilter extends OncePerRequestFilter {
    private static final String AUTHENTICATION_SCHEME_BASIC = "Basic ";

    private final AuthenticationManager authenticationManager;

    public BasicAuthenticationFilter(UserDetailsService userDetailsService) {
        this.authenticationManager = new ProviderManager(
                List.of(new DaoAuthenticationProvider(userDetailsService))
        );
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Authentication authentication = convert(request);
            if (authentication == null) {
                filterChain.doFilter(request, response);
                return;
            }
            this.authenticationManager.authenticate(authentication);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private Authentication convert(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!isValidAuthorizationHeader(authorizationHeader)) {
            return null;
        }

        String[] token = parseCredentials(authorizationHeader);
        if (token.length != 2) {
            throw new AuthenticationException();
        }

        return UsernamePasswordAuthenticationToken.unauthenticated(token[0], token[1]);
    }

    private boolean isValidAuthorizationHeader(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith(AUTHENTICATION_SCHEME_BASIC);
    }

    private String[] parseCredentials(String authorizationHeader) {
        String base64Credentials = authorizationHeader.substring(AUTHENTICATION_SCHEME_BASIC.length());
        String credentials = new String(Base64Utils.decodeFromString(base64Credentials), StandardCharsets.UTF_8);
        return credentials.split(":", 2);
    }
}
