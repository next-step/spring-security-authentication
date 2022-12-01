package nextstep.app.support;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class FormUsernamePasswordAuthenticationInterceptor implements HandlerInterceptor {

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

        final Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
