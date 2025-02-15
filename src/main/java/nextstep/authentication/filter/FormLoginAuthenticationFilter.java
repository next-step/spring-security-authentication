package nextstep.authentication.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.authentication.Authentication;
import nextstep.authentication.AuthenticationManager;
import nextstep.authentication.UsernamePasswordAuthenticationToken;
import nextstep.authentication.context.SecurityContextHolder;
import nextstep.authentication.exception.AuthenticationException;
import nextstep.authentication.util.matcher.MvcRequestMatcher;
import org.springframework.http.HttpMethod;

import java.io.IOException;

public class FormLoginAuthenticationFilter implements Filter {

    private static final MvcRequestMatcher DEFAULT_REQUEST_MATCHER = new MvcRequestMatcher(HttpMethod.POST, "/login");

    private final AuthenticationManager authenticationManager;

    public FormLoginAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (!DEFAULT_REQUEST_MATCHER.matches((HttpServletRequest) servletRequest)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        try {

            Authentication authenticationResult = attemptAuthentication(request, response);
            if (authenticationResult == null) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }

            SecurityContextHolder.getContext().setAuthentication(authenticationResult);

        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password);

        return authenticationManager.authenticate(authRequest);
    }
}
