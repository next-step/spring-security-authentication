package nextstep.security.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextstep.security.model.UserDetails;
import nextstep.security.service.UserDetailService;
import nextstep.app.ui.AuthenticationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
@Component
public class BasicAuthInterceptor implements HandlerInterceptor {
    private final UserDetailService userDetailService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!authenticate(authorization)) {
            throw new AuthenticationException();
        }
        return true;
    }

    private boolean authenticate(String authorization) {
        if (StringUtils.isEmpty(authorization)) {
            return false;
        }

        if (!StringUtils.startsWith(authorization, "Basic ")) {
            return false;
        }

        String encodedCredentials = authorization.substring("Basic ".length());
        String decodedCredentials = new String(Base64Utils.decodeFromString(encodedCredentials), StandardCharsets.UTF_8);

        String[] userDetail = decodedCredentials.split(":",2);

        if (userDetail.length != 2) {
            return false;
        }

        UserDetails loginMember = userDetailService.getUserDetails(userDetail[0], userDetail[1]);
        if (loginMember == null) {
            return false;
        }

        log.info("Login success. ID : {} / password : {}", loginMember.getUsername(), loginMember.getPassword());
        return true;
    }
}
