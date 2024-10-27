package nextstep.security.authentication;

import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import nextstep.app.ui.AuthenticationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class FormLoginAuthenticationInterceptor implements HandlerInterceptor {

    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";

    private final AuthenticationManager authenticationManager;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) {
        try {
            Map<String, String[]> paramMap = request.getParameterMap();
            String email = paramMap.get("username")[0];
            String password = paramMap.get("password")[0];

            Authentication authentication = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(email, password));

            if (Objects.isNull(authentication) || !authentication.isAuthenticated()) {
                throw new AuthenticationException();
            }

            request.getSession().setAttribute(SPRING_SECURITY_CONTEXT_KEY, authentication);

            return false;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }
}
