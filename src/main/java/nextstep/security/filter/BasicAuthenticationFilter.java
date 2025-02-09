package nextstep.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.UsernamePasswordAuthenticationToken;
import nextstep.security.context.SecurityContext;
import nextstep.security.context.SecurityContextHolder;
import nextstep.security.exception.AuthenticationException;
import nextstep.security.util.Base64Convertor;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

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

        Authentication resultAuthentication = authenticationManager.authenticate(getAuthenticationFrom(httpRequest));
        SecurityContextHolder.getContext().setAuthentication(resultAuthentication);
        chain.doFilter(request, response);
    }

    private static boolean notTarget(HttpServletRequest httpRequest) {
        return !httpRequest.getRequestURI().startsWith("/members");
    }

    private static Authentication getAuthenticationFrom(HttpServletRequest httpRequest) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (authentication != null) {
            return authentication;
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
