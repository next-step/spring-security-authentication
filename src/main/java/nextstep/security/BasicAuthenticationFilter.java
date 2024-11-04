package nextstep.security;

import static nextstep.security.SecurityConstants.BASIC_TOKEN_PREFIX;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.app.ui.AuthenticationException;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.UsernamePasswordAuthenticationToken;
import nextstep.security.context.SecurityContext;
import nextstep.security.context.SecurityContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Base64Utils;
import org.springframework.web.filter.OncePerRequestFilter;

public class BasicAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    private final List<String> ACCEPTED_URIS = List.of(
            "/members"
    );

    public BasicAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        if (!ACCEPTED_URIS.contains(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            checkAuthentication(request);
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            SecurityContextHolder.clearContext();
        }
    }

    private void checkAuthentication(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        validateBasicToken(authorization);

        String decodedToken = decodeToken(authorization);

        Authentication authentication = authenticationManager.authenticate(
                createAuthentication(decodedToken));

        validateAuthentication(authentication);

        SecurityContext ctx = SecurityContextHolder.createEmptyContext();
        ctx.setAuthentication(authentication);
        SecurityContextHolder.setContext(ctx);
    }

    private void validateBasicToken(String authorization) {
        if (authorization == null) {
            throw new AuthenticationException();
        }

        if (!authorization.startsWith(BASIC_TOKEN_PREFIX)) {
            throw new AuthenticationException();
        }
    }

    private String decodeToken(String authorization) {
        String encodedToken = authorization.substring(BASIC_TOKEN_PREFIX.length());

        if (encodedToken.isBlank()) {
            throw new AuthenticationException();
        }
        return new String(Base64Utils.decodeFromString(encodedToken), StandardCharsets.UTF_8);
    }

    private Authentication createAuthentication(String decodedToken) {
        String[] emailAndPassword = decodedToken.split(":");

        if (emailAndPassword.length != 2) {
            throw new AuthenticationException();
        }

        return UsernamePasswordAuthenticationToken.unauthenticated(emailAndPassword[0],
                emailAndPassword[1]);
    }

    private void validateAuthentication(Authentication authentication) {
        if (authentication == null) {
            throw new AuthenticationException();
        }

        if (!authentication.isAuthenticated()) {
            throw new AuthenticationException();
        }
    }
}
