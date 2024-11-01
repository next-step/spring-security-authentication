package nextstep.security.interceptor;

import nextstep.app.ui.AuthenticationException;
import nextstep.security.context.UserContextHolder;
import nextstep.security.service.UserDetailsService;
import nextstep.security.userdetails.UserDetails;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class FormLoginAuthInterceptor implements HandlerInterceptor {

    private final UserDetailsService userDetailsService;

    public FormLoginAuthInterceptor(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<String, String[]> paramMap = request.getParameterMap();
        String email = paramMap.get("username")[0];
        String password = paramMap.get("password")[0];

        UserDetails userDetails = userDetailsService.loadUserByEmailAndPassword(email, password);
        if (userDetails == null) {
            throw new AuthenticationException();
        }
        UserContextHolder.setUser(userDetails);
        return true;
    }
}
