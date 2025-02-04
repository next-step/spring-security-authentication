package nextstep.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.UsernamePasswordAuthenticationToken;
import nextstep.security.core.context.SecurityContextRepository;

import java.io.IOException;
import java.util.Map;

public class FormAuthFilter extends AbstractAuthProcessingFilter {

    public FormAuthFilter(final AuthenticationManager authenticationManager, final SecurityContextRepository securityContextRepository) {
        super(authenticationManager, securityContextRepository);
    }

    @Override
    boolean match(final HttpServletRequest request) {
        return request.getRequestURI().equals("/login");
    }

    @Override
    public Authentication makeAuthentication(final HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        String username = parameterMap.get("username")[0];
        String password = parameterMap.get("password")[0];

        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username,
                password);
        return authRequest;
    }

    @Override
    protected void successAuthentication(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
    }
}
