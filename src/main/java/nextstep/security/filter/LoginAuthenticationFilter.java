package nextstep.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.UsernamePasswordAuthenticationToken;
import nextstep.security.context.SecurityContextHolder;
import nextstep.security.context.SecurityContextRepository;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Map;

public class LoginAuthenticationFilter extends GenericFilterBean {

    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";
    private final AuthenticationManager authenticationManager;

    public LoginAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        if (notTarget(httpRequest)) {
            chain.doFilter(request, response);
            return;
        }

        Authentication authentication = authenticationManager.authenticate(getAuthentication(httpRequest));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private static boolean notTarget(HttpServletRequest httpRequest) {
        return !httpRequest.getRequestURI().startsWith("/login");
    }

    private static UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest httpRequest) {
        Map<String, String[]> parameterMap = httpRequest.getParameterMap();
        String username = parameterMap.get("username")[0];
        String password = parameterMap.get("password")[0];
        return new UsernamePasswordAuthenticationToken(username, password);
    }
}
