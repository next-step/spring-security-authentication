package nextstep.security.filter;

import nextstep.security.context.SecurityContextHolder;
import nextstep.security.domain.UserDetails;
import nextstep.security.domain.UserDetailsService;
import org.springframework.util.Base64Utils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BasicAuthenticationFilter extends GenericFilterBean {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BASIC_PREFIX = "Basic ";

    private final UserDetailsService userDetailsService;

    public BasicAuthenticationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;

        if (!("GET".equals(servletRequest.getMethod())
                && servletRequest.getRequestURI().startsWith("/members"))) {
            chain.doFilter(request, response);
            return;
        }

        String authorizationHeader = servletRequest.getHeader(AUTHORIZATION_HEADER);

        if (!isValidAuthorizationHeader(authorizationHeader)) {
            unauthorized((HttpServletResponse) response);
            return;
        }

        String[] credentials = parseCredentials(authorizationHeader);
        if (credentials.length != 2) {
            unauthorized((HttpServletResponse) response);
            return;
        }

        UserDetails userDetails = userDetailsService.loadUserByUsernameAndPassword(credentials[0], credentials[1]);
        if (userDetails.isEmpty()) {
            unauthorized((HttpServletResponse) response);
            return;
        }

        SecurityContextHolder.setUserDetails(userDetails);
        chain.doFilter(request, response);
        return;
    }

    private boolean isValidAuthorizationHeader(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith(BASIC_PREFIX);
    }

    private String[] parseCredentials(String authorizationHeader) {
        String base64Credentials = authorizationHeader.substring(BASIC_PREFIX.length());
        String credentials = new String(Base64Utils.decodeFromString(base64Credentials), StandardCharsets.UTF_8);
        return credentials.split(":", 2);
    }

    private void unauthorized(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
