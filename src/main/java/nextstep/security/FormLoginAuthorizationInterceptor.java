package nextstep.security;

import static nextstep.security.SecurityConstants.SPRING_SECURITY_CONTEXT_KEY;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.app.ui.AuthenticationException;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.DefaultAuthentication;
import org.springframework.web.servlet.HandlerInterceptor;

public class FormLoginAuthorizationInterceptor implements HandlerInterceptor {

    private final AuthenticationManager authenticationManager;

    public FormLoginAuthorizationInterceptor(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
        try {
            Map<String, String[]> paramMap = request.getParameterMap();

            Authentication authentication = authenticationManager.authenticate(
                    createAuthentication(paramMap));

            validateAuthentication(authentication);

            request.getSession().setAttribute(SPRING_SECURITY_CONTEXT_KEY, authentication);
            return true;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }

    private Authentication createAuthentication(Map<String, String[]> paramMap) {
        return DefaultAuthentication.unauthenticated(
                paramMap.get("username")[0],
                paramMap.get("password")[0]);
    }

    private void validateAuthentication(Authentication authentication) {
        if (authentication == null) {
            throw new AuthenticationException();
        }

        if (!authentication.isAuthenticated()) {
            throw new AuthenticationException();
        }
    }

}
