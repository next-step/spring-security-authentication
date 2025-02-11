package nextstep.security.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.AuthenticationException;
import nextstep.security.config.Authentication;
import nextstep.security.config.AuthenticationManager;
import nextstep.security.config.BasicAuthenticationToken;
import nextstep.security.config.SecurityContext;
import nextstep.security.config.SecurityContextHolder;

import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class BasicAuthFilter implements Filter {

    private static final List<String> TARGET_URL = List.of("/members");

    private final AuthenticationManager authenticationManager;

    public BasicAuthFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException {
        boolean isHttpServlet = servletRequest instanceof HttpServletRequest && servletResponse instanceof HttpServletResponse;
        if (isHttpServlet) {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;

            try {
                boolean notTarget = isNotTarget(request);
                if (notTarget) {
                    filterChain.doFilter(request, response);
                    return;
                }
                // 타겟일 경우
                checkAuthentication(request);
                filterChain.doFilter(request, response);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
            return;
        }
        throw new ServletException("BasicAuthFilter only supports HTTP requests");
    }

    private boolean isNotTarget(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return TARGET_URL.stream()
                .filter(requestURI::startsWith)
                .findAny()
                .isEmpty();
    }

    private void checkAuthentication(HttpServletRequest request) {
        String authorizationHeader = getAuthorizationHeader(request);
        if (authorizationHeader == null) throw new AuthenticationException("Missing authorization header");

        Authentication authentication = authenticationManager.authenticate(new BasicAuthenticationToken(authorizationHeader));
        if (!authentication.isAuthenticated()) {
            throw new AuthenticationException();
        }
        saveToSecurityContext(authentication);
    }

    private void saveToSecurityContext(Authentication authentication) {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    private String getAuthorizationHeader(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION);
    }


}
