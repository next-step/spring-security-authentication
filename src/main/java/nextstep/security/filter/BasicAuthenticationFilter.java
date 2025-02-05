package nextstep.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.UsernamePasswordAuthenticationToken;
import nextstep.security.context.SecurityContextHolder;
import nextstep.security.util.Base64Convertor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class BasicAuthenticationFilter extends OncePerRequestFilter {
    private static final String BASIC_PREFIX = "basic ";

    private final AuthenticationManager authenticationManager;

    public BasicAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!isNotBasicAuth(authorization)) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            String[] usernameAndPassword = extractUsernameAndPassword(authorization);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(usernameAndPassword[0], usernameAndPassword[1]);
            Authentication authenticate = authenticationManager.authenticate(authenticationToken);

            SecurityContextHolder.getContext().setAuthentication(authenticate);

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }

    private String[] extractUsernameAndPassword(String authorization) {
        String base64Credentials = authorization.substring(BASIC_PREFIX.length());
        String decodedString = Base64Convertor.decode(base64Credentials);
        return decodedString.split(":");
    }

    private boolean isNotBasicAuth(String authorization) {
        if (authorization == null) {
            return false;
        }
        return authorization.toLowerCase().startsWith(BASIC_PREFIX);
    }
}
