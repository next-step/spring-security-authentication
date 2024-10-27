package nextstep.security.authentication;

import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.app.ui.AuthenticationException;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

public class BasicAuthenticationInterceptor implements HandlerInterceptor {

    private final AuthenticationManager authenticationManager;

    public BasicAuthenticationInterceptor(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            UsernamePasswordAuthenticationToken authRequest = createAuthentication(request);

            Authentication authentication = authenticationManager.authenticate(authRequest);

            if (Objects.isNull(authentication) || !authentication.isAuthenticated()) {
                throw new AuthenticationException();
            }

            return true;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }

    private UsernamePasswordAuthenticationToken createAuthentication(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(authorization)) {
            return null;
        }

        if (!checkBasicAuth(authorization)) {
            return null;
        }

        String credential = extractCredential(authorization);
        String decodedCredential = new String(Base64Utils.decodeFromString(credential));
        String[] emailAndPassword = decodedCredential.split(":");

        String email = emailAndPassword[0];
        String password = emailAndPassword[1];

        return UsernamePasswordAuthenticationToken.unauthenticated(email, password);
    }

    private boolean checkBasicAuth(String authorization) {
        String[] split = authorization.split(" ");
        if (split.length != 2) {
            throw new AuthenticationException();
        }

        String type = split[0];
        return "Basic".equalsIgnoreCase(type);
    }

    private String extractCredential(String authorization) {
        String[] split = authorization.split(" ");
        if (split.length != 2) {
            throw new AuthenticationException();
        }

        return split[1];
    }
}
