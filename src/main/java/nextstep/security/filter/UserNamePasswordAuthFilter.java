package nextstep.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.exception.AuthenticationException;
import nextstep.security.user.UsernamePasswordAuthenticationToken;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Map;

public class UserNamePasswordAuthFilter extends GenericFilterBean {

    private final AuthenticationManager authenticationManager;

    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";

    public UserNamePasswordAuthFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            checkByUserNamePassword((HttpServletRequest) servletRequest);
        } catch (AuthenticationException e) {
            ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            return;
        }

        ((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_OK);
    }

    private void checkByUserNamePassword(HttpServletRequest httpRequest) {
        Map<String, String[]> parameterMap = httpRequest.getParameterMap();
        String username = parameterMap.get("username")[0];
        String password = parameterMap.get("password")[0];

        UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken.unAuthorizedToken(username, password);
        Authentication authenticate = authenticationManager.authenticate(authentication);
        if (authenticate.isAuthenticated()) {
            throw new AuthenticationException();
        }

        HttpSession session = httpRequest.getSession();
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, authenticate.toString());
    }
}
