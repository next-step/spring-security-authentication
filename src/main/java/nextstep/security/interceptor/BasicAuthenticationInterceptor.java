package nextstep.security.interceptor;

import nextstep.security.service.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

@Component
public class BasicAuthenticationInterceptor implements HandlerInterceptor {
    private final UserDetailsService userDetailsService;

    public BasicAuthenticationInterceptor(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorizationHeader = request.getHeader("Authorization");

        if (!isValidAuthorizationHeader(authorizationHeader)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        String[] credentials = parseCredentials(authorizationHeader);
        if (credentials.length != 2) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        String username = credentials[0];
        String password = credentials[1];
        userDetailsService.authenticate(username, password);
        return true;
    }

    private boolean isValidAuthorizationHeader(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith("Basic ");
    }

    private String[] parseCredentials(String authorizationHeader) {
        String base64Credentials = authorizationHeader.substring("Basic ".length());
        String credentials = new String(Base64Utils.decodeFromString(base64Credentials), StandardCharsets.UTF_8);
        return credentials.split(":", 2);
    }
}
