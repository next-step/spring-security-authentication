package nextstep.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.app.util.Base64Convertor;
import nextstep.security.Authentication;
import nextstep.security.AuthenticationManager;
import nextstep.security.UsernamePasswordAuthenticationToken;
import nextstep.security.exception.AuthenticationException;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class BasicAuthFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private static final String AUTHORIZATION_HEADER = "Authorization";

    public BasicAuthFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !HttpMethod.GET.name().equalsIgnoreCase(request.getMethod());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            authCheck(request);
        } catch (AuthenticationException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void authCheck(HttpServletRequest httpRequest) {
        String authorization = httpRequest.getHeader(AUTHORIZATION_HEADER);
        if (!StringUtils.hasText(authorization)) {
            throw new AuthenticationException();
        }

        String credentials = authorization.split(" ")[1];
        String decodedString = Base64Convertor.decode(credentials);
        String[] usernameAndPassword = decodedString.split(":");
        if (usernameAndPassword.length != 2) {
            throw new AuthenticationException();
        }

        String username = usernameAndPassword[0];
        String password = usernameAndPassword[1];
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            throw new AuthenticationException();
        }

        UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken.unAuthorizedToken(username, password);
        Authentication authenticate = authenticationManager.authenticate(authentication);
        if (authenticate.isAuthenticated()) {
            throw new AuthenticationException();
        }
    }
}
