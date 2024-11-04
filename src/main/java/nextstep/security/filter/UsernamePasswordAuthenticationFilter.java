package nextstep.security.filter;

import nextstep.security.authentication.*;
import nextstep.security.domain.UserDetailsService;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class UsernamePasswordAuthenticationFilter extends GenericFilterBean {
    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";
    private static final String DEFAULT_REQUEST_URI = "/login";

    private final AuthenticationManager authenticationManager;

    public UsernamePasswordAuthenticationFilter(UserDetailsService userDetailsService) {
        this.authenticationManager = new ProviderManager(
                List.of(new DaoAuthenticationProvider(userDetailsService))
        );
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!DEFAULT_REQUEST_URI.equals(((HttpServletRequest) request).getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        try {
            Authentication authentication = convert(request);
            if (authentication == null) {
                chain.doFilter(request, response);
                return;
            }

            Authentication authenticate = authenticationManager.authenticate(authentication);

            HttpSession session = ((HttpServletRequest) request).getSession();
            session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, authenticate);
        } catch (Exception e) {
            ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private Authentication convert(ServletRequest servletRequest) {
        try {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            return UsernamePasswordAuthenticationToken.unauthenticated(username, password);
        } catch (Exception e) {
            return null;
        }
    }
}
