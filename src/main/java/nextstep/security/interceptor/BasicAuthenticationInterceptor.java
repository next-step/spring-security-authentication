package nextstep.security.interceptor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class BasicAuthenticationInterceptor implements HandlerInterceptor {
    private static final String BASIC_AUTH = "Basic";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith(BASIC_AUTH)) {
            String[] values = getBasicValues(authorizationHeader);
            if (values.length == 2 && values[0].equals(USERNAME) && values[1].equals(PASSWORD)) {
                return true; // 인증 성공 시 요청을 진행
            }
        }

        // 인증 실패 시 401 상태 코드를 반환
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return false;
    }

    private String[] getBasicValues(String authorizationHeader) {
        String base64Credentials = authorizationHeader.substring(BASIC_AUTH.length()).trim();
        String credentials = new String(Base64.getDecoder().decode(base64Credentials), StandardCharsets.UTF_8);
        return credentials.split(":", 2);
    }
}
