package nextstep.security.filter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.UsernamePasswordAuthenticationToken;
import nextstep.security.authentication.context.SecurityContextHolder;
import nextstep.security.authentication.context.SecurityContextImpl;
import nextstep.security.authentication.context.SecurityContextRepository;
import nextstep.security.authentication.context.SessionSecurityContextRepository;
import nextstep.security.authentication.provider.AuthenticationManager;

import java.io.IOException;

public class FormLoginAuthenticationFilter extends AbstractAuthenticationFilter {

    private static final String SPRING_SECURITY_FORM_USERNAME = "username";
    private static final String SPRING_SECURITY_FORM_PASSWORD = "password";

    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    public FormLoginAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = new SessionSecurityContextRepository();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(!support(request)) {
            return null;
        }

        final String username = request.getParameter(SPRING_SECURITY_FORM_USERNAME);
        final String password = request.getParameter(SPRING_SECURITY_FORM_PASSWORD);

        Authentication authentication = this.authenticationManager.authentication(
                new UsernamePasswordAuthenticationToken(username, password));

        SecurityContextImpl securityContext = SecurityContextImpl.from(authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        securityContextRepository.saveContext(securityContext, request);

        return authentication;
    }

    @Override
    public boolean support(HttpServletRequest request) {
        return request.getMethod().equals("POST") &&
                request.getPathInfo().equals("/login");
    }
}
