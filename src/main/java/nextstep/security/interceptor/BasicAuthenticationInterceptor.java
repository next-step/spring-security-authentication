package nextstep.security.interceptor;

import nextstep.security.service.UserDetailsService;
import nextstep.security.userdetails.UserDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class BasicAuthenticationInterceptor implements HandlerInterceptor {

    public static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";
    private static final String BASIC = "Basic";
    private static final String BLANK = " ";
    private static final String COLON = ":";

    private final UserDetailsService userDetailsService;

    public BasicAuthenticationInterceptor(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String[] authValue = parseBasicHeader(request);
        if (authValue == null) {
            return true;
        }

        UserDetails userDetails = userDetailsService.loadUserByEmailAndPassword(authValue[0], authValue[1]);
        if (userDetails != null) {
            RequestContextHolder.getRequestAttributes().setAttribute(SPRING_SECURITY_CONTEXT, userDetails, RequestAttributes.SCOPE_SESSION);
        }
        return true;
    }

    @Nullable
    private String[] parseBasicHeader(HttpServletRequest request) {
        // authorization 헤더를 조회한다.
        String authorizationValue = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationValue == null || authorizationValue.isBlank()) {
            return null;
        }

        // Basic authentication 방식인지 확인한다.
        String[] strs = authorizationValue.split(BLANK);
        if (strs.length != 2) {
            return null;
        }

        String authType = strs[0];
        String authValue = strs[1];

        // Basic authentication 방식이 아닌 경우 null을 반환한다.
        if (!BASIC.equalsIgnoreCase(authType)) {
            return null;
        }

        String decodedBasicValue = new String(Base64.getDecoder().decode(authValue), StandardCharsets.UTF_8);
        if (decodedBasicValue.isBlank()) {
            return null;
        }

        strs = decodedBasicValue.split(COLON);
        if (strs.length != 2) {
            return null;
        }

        return new String[]{strs[0], strs[1]};
    }
}
