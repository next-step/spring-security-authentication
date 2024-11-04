package nextstep.security.filter;

import lombok.RequiredArgsConstructor;
import nextstep.security.core.SecurityPrincipal;
import nextstep.security.core.authentication.Authentication;
import nextstep.security.core.authentication.AuthenticationManager;
import nextstep.security.core.authentication.provider.BasicTokenAuthenticationToken;
import nextstep.security.exception.AuthErrorCodes;
import nextstep.security.exception.AuthenticationException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class BasicTokenAuthenticationFilter implements Filter {
    private final AuthenticationManager authenticationManager;

    private static final SecurityPrincipal PRINCIPAL = SecurityPrincipal.BASIC_TOKEN_AUTHENTICATION;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String basic = httpServletRequest.getHeader("authorization");
        try {
            Authentication authentication = new Authentication(
                    new BasicTokenAuthenticationToken(basic),
                    SecurityPrincipal.BASIC_TOKEN_AUTHENTICATION,
                    false
            );

            authentication = authenticationManager.getAuthenticationProvider(PRINCIPAL).authenticate(authentication);
            if (authentication.isAuthenticated()) {
                chain.doFilter(request, response);
                return;
            }
        }catch (Exception e){

        }

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
