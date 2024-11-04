package nextstep.security.filter;

import nextstep.security.context.UserContextHolder;
import nextstep.security.service.UserDetailsService;
import nextstep.security.userdetails.UserDetails;

import javax.servlet.*;
import java.io.IOException;
import java.util.Map;

public class FormLoginAuthFilter implements Filter {

    private final UserDetailsService userDetailsService;

    public FormLoginAuthFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Map<String, String[]> paramMap = request.getParameterMap();
        String email = paramMap.get("username")[0];
        String password = paramMap.get("password")[0];

        UserDetails userDetails = userDetailsService.loadUserByEmailAndPassword(email, password);
        if (userDetails != null) {
            UserContextHolder.setUser(userDetails);
        }
        chain.doFilter(request, response);
    }
}
