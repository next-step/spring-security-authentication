package nextstep.security.authentication;

import nextstep.security.support.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public
class FormAuthenticationInterceptor implements HandlerInterceptor {

    private final UserAuthenticationService userAuthenticationService;
    private final FormAuthenticationProvider formAuthenticationProvider;

    FormAuthenticationInterceptor(UserAuthenticationService userAuthenticationService, FormAuthenticationProvider formAuthenticationProvider) {
        this.userAuthenticationService = userAuthenticationService;
        this.formAuthenticationProvider = formAuthenticationProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());

        Map<String, String[]> paramMap = request.getParameterMap();
        String email = paramMap.get("username")[0];
        String password = paramMap.get("password")[0];

        userAuthenticationService.validateMember(email, password);

        formAuthenticationProvider.doAuthentication(email, password);

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
