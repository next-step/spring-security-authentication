package nextstep.security.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.config.Authentication;
import nextstep.security.config.AuthenticationManager;
import nextstep.security.config.SecurityContext;
import nextstep.security.config.SecurityContextHolder;
import nextstep.security.config.UsernamePasswordAuthenticationToken;

import java.io.IOException;
import java.util.List;

public class UsernamePasswordAuthFilter implements Filter {
    
    private static final List<String> targetURIList = List.of("/login");

    private final AuthenticationManager authenticationManager;

    public UsernamePasswordAuthFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        boolean isHttpServlet = servletRequest instanceof HttpServletRequest && servletResponse instanceof HttpServletResponse;
        if (isHttpServlet) {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;

            boolean isNotUsernamePasswordAuthTarget = checkIfAuthTarget(request);
            if (isNotUsernamePasswordAuthTarget) {
                filterChain.doFilter(request, response);
                return;
            }
            processLogin(request, response);
            return;
        }
        throw new ServletException("UsernamePasswordAuthFilter only supports HTTP requests");
    }

    private boolean checkIfAuthTarget(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return targetURIList.stream()
                .filter(requestURI::startsWith)
                .findAny()
                .isEmpty();
    }

    private void processLogin(HttpServletRequest request, HttpServletResponse response) {
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);

            Authentication authenticationResult = this.authenticationManager.authenticate(authentication);
            if (authenticationResult == null || !authenticationResult.isAuthenticated()) {
                throw new ServletException("AuthenticationManager should not return null");
            }
            addMemberToSession(authenticationResult);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private void addMemberToSession(Authentication authentication) {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

}
