package nextstep.security;

import static nextstep.security.SecurityConstants.BASIC_TOKEN_PREFIX;
import static nextstep.security.SecurityConstants.SPRING_SECURITY_CONTEXT_KEY;

import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.app.ui.AuthenticationException;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.DefaultAuthentication;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Base64Utils;
import org.springframework.web.servlet.HandlerInterceptor;

public class BasicAuthorizationInterceptor implements HandlerInterceptor {

    private final AuthenticationManager authenticationManager;

    public BasicAuthorizationInterceptor(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
        try {
            String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

            validateBasicToken(authorization);

            String decodedToken = decodeToken(authorization);

            Authentication authentication = authenticationManager.authenticate(
                    createAuthentication(decodedToken));

            validateAuthentication(authentication);

            request.getSession().setAttribute(SPRING_SECURITY_CONTEXT_KEY, authentication);
            return true;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
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

        return DefaultAuthentication.unauthenticated(emailAndPassword[0], emailAndPassword[1]);
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
