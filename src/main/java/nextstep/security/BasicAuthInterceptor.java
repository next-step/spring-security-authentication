package nextstep.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class BasicAuthInterceptor implements HandlerInterceptor {
    private final UserDetailsService userDetailsService;

    public BasicAuthInterceptor(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        String authorization = request.getHeader("Authorization");

        try {
            String credentials = authorization.split(" ")[1];
            String decodedString = Base64Convertor.decode(credentials);
            String[] usernameAndPassword = decodedString.split(":");
            String username = usernameAndPassword[0];
            String password = usernameAndPassword[1];

            final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (!userDetails.getPassword().equals(password)) {
                throw new AuthenticationException();
            }

            return true;
        } catch (Exception e) {
            throw new AuthenticationException();
        }
    }
}
