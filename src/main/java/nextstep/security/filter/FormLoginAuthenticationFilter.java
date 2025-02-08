package nextstep.security.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.security.exception.AuthenticationException;
import nextstep.security.UserDetails;
import nextstep.security.UserDetailsService;
import nextstep.security.util.matcher.MvcRequestMatcher;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.util.Map;

public class FormLoginAuthenticationFilter implements Filter {

    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";
    private static final MvcRequestMatcher DEFAULT_REQUEST_MATCHER = new MvcRequestMatcher(HttpMethod.POST, "/login");

    private final UserDetailsService userDetailsService;

    public FormLoginAuthenticationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
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
            Map<String, String[]> parameterMap = request.getParameterMap();
            String username = parameterMap.get("username")[0];
            String password = parameterMap.get("password")[0];

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (!userDetails.getPassword().equals(password)) {
                throw new AuthenticationException();
            }

            HttpSession session = request.getSession();
            session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, userDetails);

            filterChain.doFilter(servletRequest, servletResponse);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
