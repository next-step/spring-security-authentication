package nextstep.security.web.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class UsernamePasswordAuthenticationInterceptor implements HandlerInterceptor {

    //app 패키지는 security 패키지에 의존할 수 있지만, security 패키지는 app 패키지에 의존하지 않도록 한다..
//    private final LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       /* String email = request.getParameter("username");
        String password = request.getParameter("password");

        Member loginMember = loginService.login(email, password);
        if (loginMember == null) {
            return false;
        }

        request.getSession().setAttribute("member", loginMember);
        return true;*/
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}