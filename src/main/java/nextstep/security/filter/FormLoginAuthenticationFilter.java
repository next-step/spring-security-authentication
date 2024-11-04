package nextstep.security.filter;

import static nextstep.security.authentication.FormLoginAuthenticationInterceptor.SPRING_SECURITY_CONTEXT_KEY;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import nextstep.app.ui.AuthenticationException;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.filter.GenericFilterBean;

public class FormLoginAuthenticationFilter extends GenericFilterBean {

    private final AuthenticationManager authenticationManager;

    public FormLoginAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        Map<String, String[]> paramMap = request.getParameterMap();
        String email = paramMap.get("username")[0];
        String password = paramMap.get("password")[0];

        Authentication authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(email, password));

        if (Objects.isNull(authentication) || !authentication.isAuthenticated()) {
            throw new AuthenticationException();
        }

        ((HttpServletRequest) request).getSession()
                .setAttribute(SPRING_SECURITY_CONTEXT_KEY, authentication);
    }
}
