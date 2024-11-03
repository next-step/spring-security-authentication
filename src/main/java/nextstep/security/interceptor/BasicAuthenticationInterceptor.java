package nextstep.security.interceptor;

import nextstep.security.constants.Constants;
import nextstep.security.model.EmailPasswordAuth;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Component
public class BasicAuthenticationInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        System.out.println("BasicAuthenticationInterceptor preHandle");
        String authorization = request.getHeader(Constants.Http.AUTHORIZATION);
        if (Objects.isNull(EmailPasswordAuth.from(authorization))) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        return true;
    }
}
