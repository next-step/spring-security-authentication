package nextstep.security.interceptor;

import nextstep.app.domain.Member;
import nextstep.app.domain.UserDetailService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static nextstep.security.constants.SecurityConstants.BASIC_AUTH_HEADER;

public class BasicAuthenticationInterceptor implements HandlerInterceptor {
    private final UserDetailService userDetailService;

    public BasicAuthenticationInterceptor(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith(BASIC_AUTH_HEADER)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "인증되지 않은 사용자입니다.");
            return false;
        }

        String[] values = getBasicValues(authorizationHeader);
        if (values.length != 2) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "인증되지 않은 사용자입니다.");
            return false;
        }

        String username = values[0];
        String password = values[1];

        Member member = userDetailService.retrieveMemberByEmailAndPassword(username, password);
        if (member == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        return true;
    }

    private String[] getBasicValues(String authorizationHeader) {
        String base64Credentials = authorizationHeader.substring(BASIC_AUTH_HEADER.length()).trim();
        String credentials = new String(Base64.getDecoder().decode(base64Credentials), StandardCharsets.UTF_8);
        return credentials.split(":", 2);
    }
}
