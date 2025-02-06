package nextstep.security.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.AuthenticationException;
import nextstep.security.util.Base64Convertor;
import nextstep.security.UserDetails;
import nextstep.security.UserDetailsService;

import java.io.IOException;

public class BasicAuthenticationFilter implements Filter {

    private final UserDetailsService userDetailsService;

    public BasicAuthenticationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        try {
            String authorization = request.getHeader("Authorization");
            String credentials = authorization.split(" ")[1];
            String decodedString = Base64Convertor.decode(credentials);
            String[] usernameAndPassword = decodedString.split(":");
            String username = usernameAndPassword[0];
            String password = usernameAndPassword[1];

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (!userDetails.getPassword().equals(password)) {
                throw new AuthenticationException();
            }

            filterChain.doFilter(servletRequest, servletResponse);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
