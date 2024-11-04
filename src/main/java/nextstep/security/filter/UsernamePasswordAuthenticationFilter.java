package nextstep.security.filter;

import nextstep.security.context.SecurityContextHolder;
import nextstep.security.domain.UserDetails;
import nextstep.security.domain.UserDetailsService;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UsernamePasswordAuthenticationFilter extends GenericFilterBean {
    private final UserDetailsService userDetailsService;

    public UsernamePasswordAuthenticationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        if (SecurityContextHolder.getUserDetails() != null) {
            filterChain.doFilter(request, servletResponse);
            return;
        }

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UserDetails userDetails = userDetailsService.loadUserByUsernameAndPassword(username, password);
        if (userDetails.isEmpty()) {
            ((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        SecurityContextHolder.setUserDetails(userDetails);
    }
}
