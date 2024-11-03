package nextstep.security.interceptor;

import nextstep.security.constants.Constants;
import nextstep.security.model.EmailPasswordAuth;
import nextstep.security.service.MemberValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Component
public class MemberValidationInterceptor implements HandlerInterceptor {
    private final MemberValidationService memberValidationService;
    @Autowired
    public MemberValidationInterceptor(MemberValidationService memberValidationService) {
        this.memberValidationService = memberValidationService;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        System.out.println("MemberValidationInterceptor preHandle");
        String authorization = request.getHeader(Constants.Http.AUTHORIZATION);
        EmailPasswordAuth emailPasswordAuth = EmailPasswordAuth.from(authorization);
        if (Objects.isNull(emailPasswordAuth)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        if(memberValidationService.isValidMember(emailPasswordAuth.getEmail(),
                emailPasswordAuth.getPassword())) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        return true;
    }
}
