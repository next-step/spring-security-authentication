package nextstep.security.interceptor;

import nextstep.security.context.SecurityContextHolder;
import nextstep.security.domain.UserDetails;
import nextstep.security.domain.UserDetailsService;
import nextstep.security.exception.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

@Component
public class BasicAuthenticationInterceptor implements HandlerInterceptor {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BASIC_PREFIX = "Basic ";

    private final UserDetailsService userDetailsService;

    public BasicAuthenticationInterceptor(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (!isValidAuthorizationHeader(authorizationHeader)) {
            return unauthorized(response);
        }

        String[] credentials = parseCredentials(authorizationHeader);
        if (credentials.length != 2) {
            return unauthorized(response);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsernameAndPassword(credentials[0], credentials[1]);
        if (userDetails.isEmpty()) {
            throw new AuthenticationException();
        }

        SecurityContextHolder.setUserDetails(userDetails);
        return true;
    }

    private boolean isValidAuthorizationHeader(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith(BASIC_PREFIX);
    }

    private String[] parseCredentials(String authorizationHeader) {
        String base64Credentials = authorizationHeader.substring(BASIC_PREFIX.length());
        String credentials = new String(Base64Utils.decodeFromString(base64Credentials), StandardCharsets.UTF_8);
        return credentials.split(":", 2);
    }

    private boolean unauthorized(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }
}
