package nextstep.security.filter;

import nextstep.security.context.UserContextHolder;
import nextstep.security.userdetails.UserDetails;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthorizationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        UserDetails userDetails = UserContextHolder.getUser();
        if (userDetails == null) {
            ((HttpServletResponse) response).setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        chain.doFilter(request, response);
    }
}
