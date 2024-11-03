package nextstep.security.authentication;

import static nextstep.security.authentication.SecurityConstants.SPRING_SECURITY_CONTEXT_KEY;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.security.exception.AuthenticationException;
import nextstep.security.userdetail.UserDetail;
import nextstep.security.userdetail.UserDetailService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.HandlerInterceptor;

public class BasicAuthInterceptor implements HandlerInterceptor {

    private final TokenDecoder tokenDecoder;
    private final UserDetailService userDetailService;


    public BasicAuthInterceptor(TokenDecoder tokenDecoder, UserDetailService userDetailService) {
        this.tokenDecoder = tokenDecoder;
        this.userDetailService = userDetailService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
        try {
            String token = request.getHeader(HttpHeaders.AUTHORIZATION);

            UserDetail decodedUserInfo = tokenDecoder.decodeToken(token);

            UserDetail userDetail = userDetailService.getUserDetail(decodedUserInfo.getUsername());

            if (!userDetail.verifyPassword(decodedUserInfo.getPassword())) {
                throw new AuthenticationException();
            }

            request.getSession().setAttribute(SPRING_SECURITY_CONTEXT_KEY, userDetail);
            return true;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }
}
