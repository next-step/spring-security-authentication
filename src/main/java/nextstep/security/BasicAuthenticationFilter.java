package nextstep.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.app.ui.AuthenticationException;
import nextstep.app.util.Base64Convertor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class BasicAuthenticationFilter extends OncePerRequestFilter {
    public static final String USERNAME_PASSWORD_SEPARATOR = ":";

    private final UserDetailsService userDetailsService;

    public BasicAuthenticationFilter(final UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        try {
            checkAuthentication(request);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private void checkAuthentication(final HttpServletRequest request) {
        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String[] authTypeAndCredential = authorization.split(" ");

        checkBasicAuthHeader(authTypeAndCredential);

        final String credentials = authTypeAndCredential[1];
        final String decodedString = Base64Convertor.decode(credentials);
        final String[] usernameAndPassword = decodedString.split(USERNAME_PASSWORD_SEPARATOR);

        final String username = usernameAndPassword[0];
        final String password = usernameAndPassword[1];

        checkValidUserDetails(username, password);
    }

    private void checkValidUserDetails(final String username, final String password) {
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!userDetails.getPassword().equals(password)) {
            throw new AuthenticationException();
        }
    }

    private void checkBasicAuthHeader(final String[] authTypeAndCredential) {
        final String type = authTypeAndCredential[0];

        if (!type.equals("Basic")) {
            throw new AuthenticationException();
        }
    }
}
