package nextstep.security.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.security.AuthenticationException;
import nextstep.security.UserDetailService;
import nextstep.security.UserDetails;

import java.io.IOException;
import java.util.List;

public class UsernamePasswordAuthFilter implements Filter {

    private static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";
    private static final List<String> targetURIList = List.of("/login");

    private final UserDetailService userDetailService;

    public UsernamePasswordAuthFilter(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
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

            UserDetails userDetail = userDetailService.getUserByUsername(username);
            if (!userDetail.getPassword().equals(password)) {
                throw new AuthenticationException();
            }
            addMemberToSession(request, userDetail);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private void addMemberToSession(HttpServletRequest request, UserDetails userDetail) {
        HttpSession session = request.getSession();
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, userDetail);
    }

}
