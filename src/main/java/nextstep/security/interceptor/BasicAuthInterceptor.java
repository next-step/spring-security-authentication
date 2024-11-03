package nextstep.security.interceptor;

import nextstep.security.model.UserDetail;
import nextstep.security.service.UserDetailService;
import org.springframework.http.HttpStatus;
import org.springframework.util.Base64Utils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;

public class BasicAuthInterceptor implements HandlerInterceptor {
    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";
    private final UserDetailService userDetailService;

    public BasicAuthInterceptor(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String basicToken = request.getHeader("Authorization");
        HttpSession session = request.getSession();

        if (!isAuthentication(basicToken, session)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        return true;
    }


    private boolean isAuthentication(String token, HttpSession session) {
        String payload = token.split(" ")[1];

        String decodedPayload = new String(Base64Utils.decodeFromString(payload), StandardCharsets.UTF_8);
        String[] memberInfo = decodedPayload.split(":");

        if (memberInfo.length != 2) {
            return false;
        }

        UserDetail userDetail = userDetailService.isValidUser(memberInfo[0], memberInfo[1]);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, userDetail);
        return true;
    }


}
