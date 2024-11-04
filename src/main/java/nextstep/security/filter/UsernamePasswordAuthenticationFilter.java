package nextstep.security.filter;

import lombok.RequiredArgsConstructor;
import nextstep.security.core.authentication.provider.UsernamePasswordAuthenticationToken;
import nextstep.security.core.SecurityPrincipal;
import nextstep.security.core.authentication.Authentication;
import nextstep.security.core.authentication.AuthenticationManager;
import nextstep.security.core.authentication.AuthenticationProvider;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class UsernamePasswordAuthenticationFilter implements Filter {
    private static final SecurityPrincipal PRINCIPAL = SecurityPrincipal.USERNAME_PASSWORD_AUTHENTICATION;
    private final AuthenticationManager manager;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String username = httpServletRequest.getParameter("username");
        String password = httpServletRequest.getParameter("password");
        Authentication usernamePasswordAuth = new Authentication(
                new UsernamePasswordAuthenticationToken(username, password),
                PRINCIPAL,
                false
        );

        AuthenticationProvider provider = manager.getAuthenticationProvider(PRINCIPAL);
        Authentication result = provider.authenticate(usernamePasswordAuth);

        if(result.isAuthenticated())
            chain.doFilter(request, response);

        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpServletResponse.flushBuffer();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
