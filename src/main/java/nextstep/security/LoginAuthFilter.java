package nextstep.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class LoginAuthFilter extends GenericFilterBean {

    private final UserDetailService userDetailService;

    private static final String[] TARGET_PATH = new String[] { "/login" };
    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";

    public LoginAuthFilter(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest httpRequest) {
            boolean isPostMethod = HttpMethod.POST.name().equalsIgnoreCase(httpRequest.getMethod());
            boolean isMatchedPath = Arrays.stream(TARGET_PATH).anyMatch(it -> it.equalsIgnoreCase(httpRequest.getRequestURI()));
            if (isPostMethod && isMatchedPath) {
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
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void login(HttpServletRequest httpRequest) {
        Map<String, String[]> parameterMap = httpRequest.getParameterMap();
        String username = parameterMap.get("username")[0];
        String password = parameterMap.get("password")[0];

        UserDetails userDetails = userDetailService.loadUserDetailsByUserName(username);
        boolean isNotCorrectPassword = !password.equals(userDetails.password());
        if (isNotCorrectPassword) {
            throw new AuthenticationException();
        }

        HttpSession session = httpRequest.getSession();
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, userDetails.toString());
    }
}
