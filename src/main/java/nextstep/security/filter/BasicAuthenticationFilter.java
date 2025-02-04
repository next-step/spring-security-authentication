package nextstep.security.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.app.util.Base64Convertor;
import nextstep.security.AuthenticationException;
import nextstep.security.UserDetails;
import nextstep.security.UserDetailsService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Objects;

public class BasicAuthenticationFilter implements Filter {
    private final UserDetailsService userDetailsService;
    private static final String BASIC_TYPE = "Basic";

    public BasicAuthenticationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        final String authorization = ((HttpServletRequest) request).getHeader(HttpHeaders.AUTHORIZATION);
        try {
            if (isNotBasic(authorization)) {
                filterChain.doFilter(request, response);
                return;
            }
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

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!Objects.equals(userDetails.getPassword(), password)) {
            throw new AuthenticationException();
        }
    }

    private boolean isNotBasic(String authorization) {
        return authorization == null || !BASIC_TYPE.equals(authorization.split(" ")[0]);
    }


}
