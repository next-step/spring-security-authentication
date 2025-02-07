package nextstep.security.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.UsernamePasswordAuthenticationToken;
import nextstep.security.context.SecurityContext;
import nextstep.security.context.SecurityContextHolder;
import nextstep.security.context.SecurityContextRepository;
import nextstep.security.exception.AuthenticationException;
import nextstep.security.util.Base64Convertor;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class BasicAuthenticationFilter extends GenericFilterBean {

    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    public BasicAuthenticationFilter(AuthenticationManager authenticationManager, SecurityContextRepository securityContextRepository) {
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (notTarget(httpRequest)) {
            chain.doFilter(request, response);
            return;
        }

        Authentication resultAuthentication = authenticationManager.authenticate(getAuthenticationFrom(httpRequest));
        SecurityContextHolder.getContext().setAuthentication(resultAuthentication);
        securityContextRepository.saveContext(SecurityContextHolder.getContext(), httpRequest, httpResponse);
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
