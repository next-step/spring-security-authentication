package nextstep.security.authentication;

import nextstep.security.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class FormAuthenticationInterceptor implements HandlerInterceptor {

    private final AuthenticationProvider provider;

    public FormAuthenticationInterceptor(AuthenticationProvider provider) {
        this.provider = provider;
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

        final Authentication authentication = provider.authenticate(
            UsernamePasswordAuthenticationToken.unauthenticated(
                username,
                password
            )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
