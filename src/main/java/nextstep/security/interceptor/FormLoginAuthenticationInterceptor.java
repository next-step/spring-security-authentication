package nextstep.security.interceptor;

import nextstep.security.context.SecurityContextHolder;
import nextstep.security.domain.UserDetails;
import nextstep.security.domain.UserDetailsService;
import nextstep.security.exception.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class FormLoginAuthenticationInterceptor implements HandlerInterceptor {
    private final UserDetailsService userDetailsService;

    public FormLoginAuthenticationInterceptor(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (SecurityContextHolder.getUserDetails() == null) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            UserDetails userDetails = userDetailsService.loadUserByUsernameAndPassword(username, password);
            if (userDetails.isEmpty()) {
                throw new AuthenticationException();
            }

            SecurityContextHolder.setUserDetails(userDetails);
        }

        return true;
    }
}
