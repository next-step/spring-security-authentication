package nextstep.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.app.ui.AuthenticationException;
import nextstep.app.util.Base64Convertor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Objects;

public class BasicAuthenticationFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;

    public BasicAuthenticationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        try {
            checkAuthentication(request);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private void checkAuthentication(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null) {
            return;
        }

        String[] split = authorization.split(" ");
        String type = split[0];
        String credential = split[1];

        if (!"Basic".equalsIgnoreCase(type)) {
            return;
        }

        String decodedCredential = Base64Convertor.decode(credential);
        String[] emailAndPassword = decodedCredential.split(":");

        String email = emailAndPassword[0];
        String password = emailAndPassword[1];

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if (!Objects.equals(userDetails.getPassword(), password)) {
            throw new AuthenticationException();
        }
    }


}
