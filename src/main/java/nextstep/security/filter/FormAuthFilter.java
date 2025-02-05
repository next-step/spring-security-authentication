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

public class FormAuthFilter extends AbstractAuthProcessingFilter {
    private static final String LOGIN_URI = "/login";
    private static final String HTTP_POST = "POST";
    private static final String PARAM_USERNAME = "username";
    private static final String PARAM_PASSWORD = "password";

    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    public FormAuthFilter(final AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    boolean match(final HttpServletRequest request) {
        return isFormLoginRequest(request);
    }

    private boolean isFormLoginRequest(final HttpServletRequest request) {
        return LOGIN_URI.equals(request.getRequestURI()) && HTTP_POST.equals(request.getMethod());
    }

    @Override
    public Authentication makeAuthentication(final HttpServletRequest request) {
        String username = getParameterOrDefault(request, PARAM_USERNAME);
        String password = getParameterOrDefault(request, PARAM_PASSWORD);

        return UsernamePasswordAuthenticationToken.unauthenticated(username, password);
    }

    private String getParameterOrDefault(HttpServletRequest request, String paramName) {
        String[] values = request.getParameterMap().get(paramName);
        return (values != null && values.length > 0) ? values[0].trim() : "";
    }

    @Override
    protected void successAuthentication(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        final SecurityContext context = SecurityContextHolder.getContext();
        final Authentication authentication = context.getAuthentication();
        authentication.getAuthorities().add(Role.NORMAL);

        securityContextRepository.saveContext(context, request, response);
    }
}
