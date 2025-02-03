package nextstep.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.app.util.Base64Convertor;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Arrays;

public class BasicAuthFilter extends GenericFilterBean {

    private final UserDetailService userDetailService;
    private static final String[] TARGET_PATH = new String[] { "/members" };
    private static final String AUTHORIZATION_HEADER = "Authorization";

    public BasicAuthFilter(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest httpRequest) {
            boolean isGetMethod = HttpMethod.GET.name().equalsIgnoreCase(httpRequest.getMethod());
            boolean isMatchedPath = Arrays.stream(TARGET_PATH).anyMatch(it -> it.equalsIgnoreCase(httpRequest.getRequestURI()));
            if (isGetMethod && isMatchedPath) {
                try {
                    loginCheck(httpRequest);
                } catch (AuthenticationException e) {
                    HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
                    httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
                    return;
                }
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void loginCheck(HttpServletRequest httpRequest) {
        String authorization = httpRequest.getHeader(AUTHORIZATION_HEADER);
        if (!StringUtils.hasText(authorization)) {
            throw new AuthenticationException();
        }

        String credentials = authorization.split(" ")[1];
        String decodedString = Base64Convertor.decode(credentials);
        String[] usernameAndPassword = decodedString.split(":");
        if (usernameAndPassword.length != 2) {
            throw new AuthenticationException();
        }

        String username = usernameAndPassword[0];
        String password = usernameAndPassword[1];
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            throw new AuthenticationException();
        }

        UserDetails userDetails = userDetailService.loadUserDetailsByUserName(username);
        boolean isNotCorrectPassword = !password.equals(userDetails.password());
        if (isNotCorrectPassword) {
            throw new AuthenticationException();
        }
    }
}
