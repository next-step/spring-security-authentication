package nextstep.security.filter;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.app.ui.AuthenticationException;
import nextstep.security.SecurityContext;
import nextstep.security.SecurityContextHolder;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.filter.GenericFilterBean;

public class FormLoginAuthenticationFilter extends GenericFilterBean {

    private final AuthenticationManager authenticationManager;
    private final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";

    private static final String DEFAULT_REQUEST_URI = "/login";

    public FormLoginAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (!DEFAULT_REQUEST_URI.equals(((HttpServletRequest)request).getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        try {
            Map<String, String[]> paramMap = request.getParameterMap();
            String email = paramMap.get("username")[0];
            String password = paramMap.get("password")[0];

            Authentication authentication = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(email, password));

            if (Objects.isNull(authentication) || !authentication.isAuthenticated()) {
                throw new AuthenticationException();
            }

            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            ((HttpServletRequest) request).getSession()
                    .setAttribute(SPRING_SECURITY_CONTEXT_KEY, authentication);
        } catch (Exception e) {
            ((HttpServletResponse)response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
