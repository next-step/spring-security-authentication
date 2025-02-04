package nextstep.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.app.ui.AuthenticationException;
import org.springframework.web.servlet.HandlerInterceptor;

public class BasicAuthentication implements HandlerInterceptor {

    private static final String AUTHORICATION = "Authorization";

    private final UserDetailService userDetailService;

    public BasicAuthentication(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            final String authorization = request.getHeader(AUTHORICATION);
            HttpSession session = request.getSession();

            String credentials = authorization.split(" ")[1];
            String decodedString = Base64Convertor.decode(credentials);
            String[] usernameAndPassword = decodedString.split(":");
            String username = usernameAndPassword[0];
            String password = usernameAndPassword[1];

            UserDetail userDetail = userDetailService.loadUserByUsername(username);
            if(!userDetail.getPassword().equals(password)) {
                throw new AuthenticationException();
            }

            return true;
        }catch (Exception e) {
            throw new AuthenticationException();
        }
    }
}
