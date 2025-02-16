package nextstep.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.app.util.Base64Convertor;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.DaoAuthenticationProvider;
import nextstep.security.authentication.ProviderManager;
import nextstep.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class BasicAuthenticationFilter extends OncePerRequestFilter {
    public static final String USERNAME_PASSWORD_SEPARATOR = ":";

    private final AuthenticationManager authenticationManager;

    public BasicAuthenticationFilter(final UserDetailsService userDetailsService) {
        this.authenticationManager = new ProviderManager(
                List.of(new DaoAuthenticationProvider(userDetailsService))
        );
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        try {
            final Authentication authentication = generateAuthentication(request);

            if (authentication == null) {
                filterChain.doFilter(request, response);
                return;
            }

            this.authenticationManager.authenticate(authentication);

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private Authentication generateAuthentication(final HttpServletRequest request) {
        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorization == null) {
            return null;
        }

        final String[] authTypeAndCredential = authorization.split(" ");

        final String type = authTypeAndCredential[0];

        if (!type.equals("Basic")) {
            return null;
        }

        final String credentials = authTypeAndCredential[1];
        final String decodedString = Base64Convertor.decode(credentials);
        final String[] usernameAndPassword = decodedString.split(USERNAME_PASSWORD_SEPARATOR);

        final String username = usernameAndPassword[0];
        final String password = usernameAndPassword[1];

        return new UsernamePasswordAuthenticationToken(username, password, false);
    }
}
