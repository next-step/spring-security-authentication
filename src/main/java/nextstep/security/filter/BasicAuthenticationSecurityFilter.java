package nextstep.security.filter;


import java.io.IOException;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.app.ui.AuthenticationException;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

public class BasicAuthenticationSecurityFilter extends GenericFilterBean {

    private final AuthenticationManager authenticationManager;
    private static final String DEFAULT_REQUEST_URI = "/members";

    public BasicAuthenticationSecurityFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (!DEFAULT_REQUEST_URI.equals(((HttpServletRequest) request).getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        try {
            UsernamePasswordAuthenticationToken authRequest = createAuthentication(
                    (HttpServletRequest) request);

            Authentication authentication = authenticationManager.authenticate(authRequest);

            if (Objects.isNull(authentication) || !authentication.isAuthenticated()) {
                ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }

            chain.doFilter(request, response);
        } catch (Exception e) {
            ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private UsernamePasswordAuthenticationToken createAuthentication(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(authorization)) {
            return null;
        }

        if (!checkBasicAuth(authorization)) {
            return null;
        }

        String credential = extractCredential(authorization);
        String decodedCredential = new String(Base64Utils.decodeFromString(credential));
        String[] emailAndPassword = decodedCredential.split(":");

        String email = emailAndPassword[0];
        String password = emailAndPassword[1];

        return UsernamePasswordAuthenticationToken.unauthenticated(email, password);
    }

    private boolean checkBasicAuth(String authorization) {
        String[] split = authorization.split(" ");
        if (split.length != 2) {
            throw new AuthenticationException();
        }

        String type = split[0];
        return "Basic".equalsIgnoreCase(type);
    }

    private String extractCredential(String authorization) {
        String[] split = authorization.split(" ");
        if (split.length != 2) {
            throw new AuthenticationException();
        }

        return split[1];
    }
}
