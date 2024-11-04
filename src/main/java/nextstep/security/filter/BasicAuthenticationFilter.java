package nextstep.security.filter;

import nextstep.app.ui.AuthenticationException;
import nextstep.security.service.UserDetailService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BasicAuthenticationFilter extends OncePerRequestFilter {
    private final UserDetailService userDetailsService;

    public BasicAuthenticationFilter(UserDetailService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        try {
            checkAuthentication(request);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private void checkAuthentication(HttpServletRequest request) {
        throw new AuthenticationException();
    }
}
