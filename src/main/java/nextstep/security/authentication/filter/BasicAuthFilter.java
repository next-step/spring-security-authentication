package nextstep.security.authentication.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.UsernamePasswordAuthenticationToken;
import nextstep.security.userdetail.UserDetail;
import nextstep.security.util.TokenDecoder;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.OncePerRequestFilter;

public class BasicAuthFilter extends OncePerRequestFilter {

    private final TokenDecoder tokenDecoder;
    private final AuthenticationManager authenticationManager;

    public BasicAuthFilter(TokenDecoder tokenDecoder, AuthenticationManager authenticationManager) {
        this.tokenDecoder = tokenDecoder;
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        try {

            String token = request.getHeader(HttpHeaders.AUTHORIZATION);

            UserDetail decodedUserInfo = tokenDecoder.decodeToken(token);

            UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken.unauthenticated(
                    decodedUserInfo.getUsername(), decodedUserInfo.getPassword());

            authenticationManager.authenticate(authentication);

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
