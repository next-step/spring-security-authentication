package nextstep.security.filter;

import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.UsernamePasswordAuthenticationToken;
import nextstep.security.context.UserContextHolder;
import nextstep.security.manager.AuthenticationManager;
import nextstep.security.userdetails.UserDetails;

import javax.servlet.*;
import java.io.IOException;
import java.util.Map;

public class FormLoginAuthFilter implements Filter {

    private final AuthenticationManager authenticationManager;

    public FormLoginAuthFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Map<String, String[]> paramMap = request.getParameterMap();
        String email = paramMap.get("username")[0];
        String password = paramMap.get("password")[0];

        Authentication authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        if (authentication != null) {
            UserContextHolder.setUser((UserDetails) authentication.getPrincipal());
        }
        chain.doFilter(request, response);
    }
}
