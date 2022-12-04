package nextstep.security.authentication;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public
class FormAuthenticationInterceptor implements HandlerInterceptor {

    private final AuthenticationProvider authenticationProvider;

    FormAuthenticationInterceptor(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        authenticationProvider.clearAuthentication();

        Map<String, String[]> paramMap = request.getParameterMap();
        String principal = paramMap.get("username")[0];
        String credentials = paramMap.get("password")[0];

        authenticationProvider.doAuthentication(new FormAuthenticationToken(principal, credentials));

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
