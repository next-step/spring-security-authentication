package nextstep.security.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.AuthenticationException;
import nextstep.security.UserDetailService;
import nextstep.security.UserDetails;
import nextstep.security.util.Base64Convertor;

import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class BasicAuthFilter implements Filter {

    private static final List<String> TARGET_URL = List.of("/members");

    private final UserDetailService userDetailsService;

    public BasicAuthFilter(UserDetailService userDetailsService) {
        this.userDetailsService = userDetailsService;
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
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader == null) {
            return;
        }
        String authType = authorizationHeader.split(" ")[0];
        String credentials = authorizationHeader.split(" ")[1];
        String decodedString = Base64Convertor.decode(credentials);
        checkAuthType(authType);

        String[] usernameAndPassword = decodedString.split(":");
        String username = usernameAndPassword[0];
        String password = usernameAndPassword[1];

        UserDetails userDetail = userDetailsService.getUserByUsername(username);
        if (!userDetail.getPassword().equals(password)) {
            throw new AuthenticationException();
        }
    }

    private void checkAuthType(String authType) {
        if (!HttpServletRequest.BASIC_AUTH.equalsIgnoreCase(authType)) {
            throw new AuthenticationException();
        }
    }

}
