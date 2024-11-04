package nextstep.security;

import nextstep.app.domain.LoginService;
import nextstep.app.domain.Member;
import nextstep.app.ui.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class FormLoginInterceptor implements HandlerInterceptor {

    private final LoginService loginService;

    public FormLoginInterceptor(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        try {
            Member loginMember = loginService.login(request.getParameter("username"), request.getParameter("password"));

            if (loginMember == null) {
                System.out.println("로그인에 실패하였습니다.");
                throw new AuthenticationException();
            }
            //로그인 성공 처리
            //세션에 로그인 회원 정보 보관
            System.out.println("로그인 성공 처리");
            HttpSession session = request.getSession();
            session.setAttribute("SPRING_SECURITY_CONTEXT", loginMember);
            ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            throw new AuthenticationException();
        }

        return true;
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> handleAuthenticationException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
