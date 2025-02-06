package nextstep.security.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.*;
import nextstep.security.exception.AuthenticationException;
import nextstep.security.util.Base64Convertor;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

import static nextstep.security.filter.LoginAuthenticationFilter.SPRING_SECURITY_CONTEXT_KEY;

public class BasicAuthenticationFilter extends GenericFilterBean {

    private final AuthenticationManager authenticationManager;

    public BasicAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if (notTarget(httpRequest)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            Authentication resultAuthentication = authenticationManager.authenticate(getAuthenticationFrom(httpRequest));
            SecurityContextHolder.getContext().setAuthentication(resultAuthentication);
        } catch (AuthenticationException e) {
            SecurityContextHolder.clearContext();
            throw e;
        }

        try {
            chain.doFilter(request, response);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    private static boolean notTarget(HttpServletRequest httpRequest) {
        return !httpRequest.getRequestURI().startsWith("/members");
    }

    private static Authentication getAuthenticationFrom(HttpServletRequest httpRequest) {
        if (httpRequest.getSession().getAttribute(SPRING_SECURITY_CONTEXT_KEY) != null) {
            SecurityContext attribute = (SecurityContext) httpRequest.getSession().getAttribute(SPRING_SECURITY_CONTEXT_KEY);
            return attribute.getAuthentication();
        }

        String basicToken = extractToken(httpRequest);
        String[] usernameAndPassword = splitToken(basicToken);

        String username = usernameAndPassword[0];
        String password = usernameAndPassword[1];
        return new UsernamePasswordAuthenticationToken(username, password);
    }

    private static String extractToken(HttpServletRequest httpRequest) {
        String authorization = httpRequest.getHeader("Authorization");
        return authorization.split(" ")[1];
    }

    private static String[] splitToken(String basicToken) {
        String decodedString = Base64Convertor.decode(basicToken);
        String[] usernameAndPassword = decodedString.split(":");
        validateToken(usernameAndPassword);
        return usernameAndPassword;
    }

    private static void validateToken(String[] usernameAndPassword) {
        if (usernameAndPassword.length != 2) {
            throw new AuthenticationException();
        }
    }
}
