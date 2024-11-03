package nextstep.security.web.authentication;

import lombok.extern.slf4j.Slf4j;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.core.Authentication;
import nextstep.security.core.AuthenticationException;
import nextstep.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Slf4j
public class UsernamePasswordAuthenticationInterceptor implements HandlerInterceptor {
    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";
    private final AuthenticationManager authenticationManager;

    public UsernamePasswordAuthenticationInterceptor() {
        this.authenticationManager = new AuthenticationManager();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // request parameter 로 전달된 정보를 인증 정보로 변환
        Authentication authentication = new Authentication(request.getParameter("username"), request.getParameter("password"), null);

        // 인증
        try {
            authentication = authenticationManager.authenticate(authentication);
        } catch (AuthenticationException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            return false;
        }

        // 인증 정보를 세션에 저장
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        request.getSession().setAttribute(SPRING_SECURITY_CONTEXT_KEY, principal);

        return true;
    }
}