package nextstep.security.interceptor;

import nextstep.security.constants.Constants;
import nextstep.security.model.EmailPasswordAuth;
import nextstep.security.service.MemberValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    private final MemberValidationService memberValidationService;
    @Autowired
    public LoginInterceptor(MemberValidationService memberValidationService) {
        this.memberValidationService = memberValidationService;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String email = request.getParameter(Constants.Auth.EMAIL);
        String password = request.getParameter(Constants.Auth.PASSWORD);
        if(memberValidationService.isValidMember(email, password)) {
            request.getSession().setAttribute(Constants.Auth.SPRING_SECURITY_CONTEXT_KEY,
                    new EmailPasswordAuth(email, password));
            return true;
        }
        return false;
    }
}
