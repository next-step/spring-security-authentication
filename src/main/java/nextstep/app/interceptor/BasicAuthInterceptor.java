package nextstep.app.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextstep.app.domain.Member;
import nextstep.app.service.MemberService;
import nextstep.app.ui.AuthenticationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Base64Utils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
public class BasicAuthInterceptor implements HandlerInterceptor {
    private final MemberService memberService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
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

        Member loginMember = memberService.getMember(userDetail[0], userDetail[1]);
        if (loginMember == null) {
            return false;
        }

        log.info("Login success. ID : {} / password : {}", loginMember.getEmail(), loginMember.getPassword());
        return true;
    }
}
