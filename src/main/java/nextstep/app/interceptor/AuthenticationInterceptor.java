package nextstep.app.interceptor;

import nextstep.security.support.Authentication;
import nextstep.security.support.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
        SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());
        return true;
    }

    @Override
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final ModelAndView modelAndView) {
        final var authentication = (Authentication) request.getAttribute("authentication");
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
