package nextstep.security.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.app.util.Base64Convertor;
import nextstep.security.Authentication;
import nextstep.security.AuthenticationException;
import nextstep.security.AuthenticationManager;
import nextstep.security.UsernamePasswordAuthenticationToken;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class BasicAuthenticationFilter implements Filter {
    private static final String BASIC_TYPE = "Basic";
    private final AuthenticationManager authenticationManager;

    public BasicAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        final String authorization = ((HttpServletRequest) request).getHeader(HttpHeaders.AUTHORIZATION);
        if (isNotBasic(authorization)) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            checkBasicAuthentication(authorization);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            ((HttpServletResponse) response).setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }

    private void checkBasicAuthentication(String authorization) {
        String credentials = authorization.split(" ")[1];
        String decodedString = Base64Convertor.decode(credentials);
        String[] usernameAndPassword = decodedString.split(":");
        String username = usernameAndPassword[0];
        String password = usernameAndPassword[1];

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        if (!authenticate.isAuthenticated()) {
            throw new AuthenticationException();
        }
    }

    private boolean isNotBasic(String authorization) {
        return authorization == null || !BASIC_TYPE.equals(authorization.split(" ")[0]);
    }
}
