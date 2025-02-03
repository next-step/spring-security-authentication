package nextstep.security.filter;

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
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Map;

public class LoginAuthFilter extends GenericFilterBean {

    private final UserDetailsService userDetailsService;

    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";

    public LoginAuthFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest httpRequest
                && (HttpMethod.POST.name().equalsIgnoreCase(httpRequest.getMethod()))
        ) {
            try {
                login(httpRequest);
            } catch (AuthenticationException e) {
                HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
                httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
                return;
            }

            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void login(HttpServletRequest httpRequest) {
        Map<String, String[]> parameterMap = httpRequest.getParameterMap();
        String username = parameterMap.get("username")[0];
        String password = parameterMap.get("password")[0];

        UserDetails userDetails = userDetailsService.loadUserDetailsByUserName(username);
        boolean isNotCorrectPassword = !password.equals(userDetails.password());
        if (isNotCorrectPassword) {
            throw new AuthenticationException();
        }

        HttpSession session = httpRequest.getSession();
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, userDetails.toString());
    }
}
