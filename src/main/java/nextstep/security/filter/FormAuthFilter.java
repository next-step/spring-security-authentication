package nextstep.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.Role;
import nextstep.security.authentication.UsernamePasswordAuthenticationToken;
import nextstep.security.core.context.HttpSessionSecurityContextRepository;
import nextstep.security.core.context.SecurityContext;
import nextstep.security.core.context.SecurityContextHolder;
import nextstep.security.core.context.SecurityContextRepository;

import java.io.IOException;
import java.util.Map;

public class FormAuthFilter extends AbstractAuthProcessingFilter {
    public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "username";

    public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";

    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    public FormAuthFilter(final AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    boolean match(final HttpServletRequest request) {
        return request.getRequestURI().equals("/login") && request.getMethod().equals("POST");
    }

    @Override
    public Authentication makeAuthentication(final HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        String username = parameterMap.get(SPRING_SECURITY_FORM_USERNAME_KEY)[0];
        String password = parameterMap.get(SPRING_SECURITY_FORM_PASSWORD_KEY)[0];
        username = (username != null) ? username.trim() : "";
        password = (password != null) ? password.trim() : "";

        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username,
                password);
        return authRequest;
    }

    @Override
    protected void successAuthentication(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        final SecurityContext context = SecurityContextHolder.getContext();
        final Authentication authentication = context.getAuthentication();
        authentication.getAuthorities().add(Role.NORMAL);

        securityContextRepository.saveContext(context, request, response);
    }
}
