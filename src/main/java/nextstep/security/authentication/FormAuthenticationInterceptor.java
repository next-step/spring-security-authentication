package nextstep.security.authentication;

import nextstep.security.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class FormAuthenticationInterceptor implements HandlerInterceptor {

    private final AuthenticationManager authenticationManager;

    public FormAuthenticationInterceptor(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public boolean preHandle(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler
    ) throws Exception {
        SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());

        final Map<String, String[]> paramMap = request.getParameterMap();
        final String username = paramMap.get("username")[0];
        final String password = paramMap.get("password")[0];

        final Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(
            username,
            password
        );

        final Authentication authenticationResult = authenticationManager.authenticate(authenticationRequest);
        SecurityContextHolder.getContext().setAuthentication(authenticationResult);

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
